package com.logistica.doisv.services;

import com.logistica.doisv.dto.ArquivoDTO;
import com.logistica.doisv.dto.CriarSolicitacaoDTO;
import com.logistica.doisv.entities.HistoricoSolicitacao;
import com.logistica.doisv.entities.ItemVenda;
import com.logistica.doisv.entities.Solicitacao;
import com.logistica.doisv.entities.Venda;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.entities.enums.StatusPedido;
import com.logistica.doisv.entities.enums.StatusSolicitacao;
import com.logistica.doisv.entities.enums.TipoSolicitacao;
import com.logistica.doisv.repositories.SolicitacaoRepository;
import com.logistica.doisv.repositories.VendaRepository;
import com.logistica.doisv.services.api.AnexoDriveService;
import com.logistica.doisv.services.exceptions.AssociacaoInvalidaException;
import com.logistica.doisv.services.exceptions.RegraNegocioException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository repository;
    private final VendaRepository vendaRepository;
    private final AnexoDriveService anexoService;

    @Transactional
    public void registrarSolicitacao(CriarSolicitacaoDTO dto, List<MultipartFile> anexos, Long idVenda) throws GeneralSecurityException, IOException {
        Venda venda = vendaRepository.findById(idVenda).orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
        validarStatusVenda(venda, dto.tipo());

        TipoSolicitacao tipoSolicitacao = TipoSolicitacao.deString(dto.tipo());
        validarPrazoSolicitacao(venda, tipoSolicitacao);

        ItemVenda itemVenda = buscarItemVendaPorId(dto.idItem(), venda.getItensVenda());
        validarQuantidade(dto.quantidade(), itemVenda.getQuantidade(), dto.tipo());

        Solicitacao solicitacao = construirSolicitacao(dto, venda, itemVenda, tipoSolicitacao);
        solicitacao = repository.save(solicitacao);

        processarAnexos(anexos, solicitacao);
    }

    @Transactional
    public void aprovarSolicitacao(Long id, Long idLoja) {
        Solicitacao solicitacao = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));

        validarAprovacaoSolicitacao(solicitacao, idLoja);

        HistoricoSolicitacao historico = criarHistorico(StatusSolicitacao.APROVADO, solicitacao,
                String.format("Solicitação de %s aprovada!", solicitacao.getTipoSolicitacao().getDescricao().toLowerCase()));

        solicitacao.setStatusSolicitacao(StatusSolicitacao.APROVADO);

        ItemVenda itemVenda = buscarItemVendaPorId(solicitacao.getItemVenda().getId(), solicitacao.getVenda().getItensVenda());

        var novaQuantidade = itemVenda.getQuantidade() - solicitacao.getQuantidade();
        itemVenda.setQuantidade(novaQuantidade);

        ItemVenda novoItemSolicitacao = ItemVenda.builder()
                .precoOriginal(itemVenda.getPrecoOriginal())
                .precoVendido(itemVenda.getPrecoVendido())
                .percentualVariacao(itemVenda.getPercentualVariacao())
                .quantidade(solicitacao.getQuantidade())
                .detalhes(String.format("Item para %s - Solicitação número: %d", solicitacao.getTipoSolicitacao().getDescricao(), solicitacao.getId()))
                .status(Status.INATIVO)
                .venda(solicitacao.getVenda())
                .produto(itemVenda.getProduto())
                .build();

        solicitacao.getVenda().getItensVenda().add(novoItemSolicitacao);
        solicitacao.getHistoricos().add(historico);
        solicitacao.setDataAtualizacao(historico.getDataAtualizacao());
        solicitacao.setItemVenda(novoItemSolicitacao);

        repository.save(solicitacao);
    }

    @Transactional
    public void reprovarSolicitacao(Long id, Long idLoja) {
        Solicitacao solicitacao = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));

        validarAprovacaoSolicitacao(solicitacao, idLoja);

        HistoricoSolicitacao historico = criarHistorico(StatusSolicitacao.REJEITADO, solicitacao,
                String.format("Solicitação de %s reprovada!", solicitacao.getTipoSolicitacao().getDescricao().toLowerCase()));

        solicitacao.getHistoricos().add(historico);
        solicitacao.setStatusSolicitacao(StatusSolicitacao.REJEITADO);
        solicitacao.setStatus(Status.INATIVO);
        solicitacao.setDataAtualizacao(historico.getDataAtualizacao());
        solicitacao.getAnexos().clear();

        repository.save(solicitacao);
    }




    private void validarStatusVenda(Venda venda, String tipo) {
        if (venda.getStatusPedido() != StatusPedido.ENTREGUE || venda.getStatus() == Status.INATIVO) {
            throw new RegraNegocioException(String.format("Não é possível realizar uma solicitação de %s com o status atual da venda", tipo));
        }
    }

    private void validarPrazoSolicitacao(Venda venda, TipoSolicitacao tipoSolicitacao) {
        int prazo = tipoSolicitacao == TipoSolicitacao.TROCA ? venda.getPrazoTroca() : venda.getPrazoDevolucao();

        LocalDate dataLimite = venda.getDataEntrega().plusDays(prazo);

        if (LocalDate.now().isAfter(dataLimite)) {
            throw new RegraNegocioException(String.format("Período para solicitar %s encerrou em %s",
                            tipoSolicitacao.getDescricao(),
                            dataLimite.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        }
    }

    private void validarQuantidade(Double quantidadeSolicitada, Double quantidadeComprada, String tipo) {
        if (quantidadeSolicitada > quantidadeComprada) {
            throw new RegraNegocioException(String.format("A quantidade selecionada para %s é maior que a quantidade comprada", tipo));
        }
    }

    private void validarAprovacaoSolicitacao(Solicitacao solicitacao, Long idLoja){
        var validarLojaSolicitacao = solicitacao.getVenda().getLoja().getIdLoja().equals(idLoja);
        var validarStatusSolicitacao = solicitacao.getStatusSolicitacao() != StatusSolicitacao.PENDENTE;
        var validarStatus = solicitacao.getStatus() == Status.INATIVO;

        if (!validarLojaSolicitacao){
            throw new AssociacaoInvalidaException("Você não tem permissão para alterar essa solicitação.");
        }
        else if (validarStatusSolicitacao || validarStatus){
            throw new RegraNegocioException(String.format("A solicitação ID %s não pode mais ser aprovada", solicitacao.getId()));
        }
    }

    private ItemVenda buscarItemVendaPorId(Long id, List<ItemVenda> itens){
        return itens.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
    }

    private void processarAnexos(List<MultipartFile> anexos, Solicitacao solicitacao) throws GeneralSecurityException, IOException {
        if (anexos != null && !anexos.isEmpty()) {
            List<ArquivoDTO> arquivos = anexos.stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .map(file -> {
                        try {
                            return new ArquivoDTO(file.getBytes(), file.getOriginalFilename(), file.getContentType());
                        } catch (IOException e) {
                            throw new RuntimeException("Erro ao ler arquivo: " + file.getOriginalFilename());
                        }
                    })
                    .toList();

            if (!arquivos.isEmpty()) {
                anexoService.processarUploadAnexos(arquivos, solicitacao);
            }
        }
    }

    private Solicitacao construirSolicitacao(CriarSolicitacaoDTO dto, Venda venda, ItemVenda itemVenda, TipoSolicitacao tipo) {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setVenda(venda);
        solicitacao.setItemVenda(itemVenda);
        solicitacao.setConsumidor(venda.getConsumidor());
        solicitacao.setTipoSolicitacao(tipo);
        solicitacao.setQuantidade(dto.quantidade());
        solicitacao.setMotivo(dto.motivo());
        solicitacao.setDataSolicitacao(Instant.now());
        return solicitacao;
    }

    private HistoricoSolicitacao criarHistorico(StatusSolicitacao statusNovo, Solicitacao solicitacao, String observacao){
        return HistoricoSolicitacao.builder()
                .statusAnterior(solicitacao.getStatusSolicitacao())
                .statusAtual(statusNovo)
                .observacao(observacao)
                .solicitacao(solicitacao)
                .dataAtualizacao(LocalDateTime.now())
                .build();
    }
}
