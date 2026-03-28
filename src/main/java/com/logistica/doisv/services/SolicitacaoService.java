package com.logistica.doisv.services;

import com.logistica.doisv.dto.ArquivoDTO;
import com.logistica.doisv.dto.registro_solicitacao.CriarSolicitacaoDTO;
import com.logistica.doisv.dto.registro_solicitacao.HistoricoSolicitacaoDTO;
import com.logistica.doisv.dto.registro_solicitacao.SolicitacaoDetalhadaDTO;
import com.logistica.doisv.dto.registro_solicitacao.SolicitacaoResumidaDTO;
import com.logistica.doisv.dto.registro_venda.requisicao.ItemDTO;
import com.logistica.doisv.dto.registro_venda.requisicao.RegistroVendaDTO;
import com.logistica.doisv.entities.ItemVenda;
import com.logistica.doisv.entities.Solicitacao;
import com.logistica.doisv.entities.Venda;
import com.logistica.doisv.entities.enums.CategoriaArquivoPermitida;
import com.logistica.doisv.entities.enums.StatusSolicitacao;
import com.logistica.doisv.entities.enums.TipoSolicitacao;
import com.logistica.doisv.repositories.SolicitacaoRepository;
import com.logistica.doisv.repositories.VendaRepository;
import com.logistica.doisv.services.api.AnexoDriveService;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
import com.logistica.doisv.util.validacao.ArquivoValidador;
import com.logistica.doisv.util.validacao.SolicitacaoValidador;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository repository;
    private final VendaRepository vendaRepository;
    private final AnexoDriveService anexoService;
    private final SolicitacaoValidador validador;
    private final VendaService vendaService;
    private final ArquivoValidador arquivoValidador;

    @Transactional(readOnly = true)
    public Page<SolicitacaoResumidaDTO> buscarTodasSolicitacoesPorLoja(Pageable pageable, Long idLoja){
        return repository.listarSolicitacoesResumidas(pageable, idLoja);
    }

    @Transactional(readOnly = true)
    public SolicitacaoDetalhadaDTO buscarSolicitacaoPorId(Long idSolicitacao, Long idLoja){
        Solicitacao solicitacao = buscarSolicitacaoOuLancarExcecao(idSolicitacao, idLoja);

        return new SolicitacaoDetalhadaDTO(solicitacao);
    }

    @Transactional
    public SolicitacaoResumidaDTO registrarSolicitacao(CriarSolicitacaoDTO dto,
                                                       List<MultipartFile> anexos,
                                                       Long idVenda,
                                                       Long idLoja) throws GeneralSecurityException, IOException {
        validarTipoAnexo(anexos);

        Venda venda = vendaRepository.buscarVendaPorId(idVenda, idLoja)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));

        TipoSolicitacao tipoSolicitacao = TipoSolicitacao.deString(dto.tipo());
        ItemVenda itemVenda = buscarItemVendaPorId(dto.idItem(), venda.getItensVenda());

        validador.validarRegistroSolicitacao(venda, dto, tipoSolicitacao, itemVenda);

        Solicitacao solicitacao = Solicitacao.criar(dto, venda, itemVenda, tipoSolicitacao);
        solicitacao = repository.save(solicitacao);

        processarAnexos(anexos, solicitacao.getId());

        return new SolicitacaoResumidaDTO(solicitacao);
    }

    @Transactional
    public SolicitacaoResumidaDTO editarSolicitacao(Long idSolicitacao, CriarSolicitacaoDTO dto, List<MultipartFile> anexos, Long idLoja) throws GeneralSecurityException, IOException {
        validarTipoAnexo(anexos);

        Solicitacao solicitacao = buscarSolicitacaoOuLancarExcecao(idSolicitacao, idLoja);

        TipoSolicitacao tipoSolicitacao = TipoSolicitacao.deString(dto.tipo());
        ItemVenda itemVenda = buscarItemVendaPorId(dto.idItem(), solicitacao.getVenda().getItensVenda());

        validador.validarEdicaoSolicitacao(solicitacao, dto, idLoja, itemVenda);

        solicitacao.editar(tipoSolicitacao, dto.quantidade(), dto.motivo());

        solicitacao = repository.save(solicitacao);
        processarAnexos(anexos, solicitacao.getId());

        return new SolicitacaoResumidaDTO(solicitacao);
    }

    @Transactional
    public SolicitacaoResumidaDTO aprovarSolicitacao(Long idSolicitacao, Long idLoja) {
        Solicitacao solicitacao = buscarSolicitacaoOuLancarExcecao(idSolicitacao, idLoja);

        ItemVenda itemOriginal = buscarItemVendaPorId(
                solicitacao.getItemVenda().getId(),
                solicitacao.getVenda().getItensVenda()
        );

        solicitacao.aprovar(itemOriginal);

        return new SolicitacaoResumidaDTO(repository.save(solicitacao));
    }

    @Transactional
    public SolicitacaoResumidaDTO reprovarSolicitacao(Long idSolicitacao, Long idLoja) throws GeneralSecurityException, IOException {
        Solicitacao solicitacao = buscarSolicitacaoOuLancarExcecao(idSolicitacao, idLoja);

        solicitacao.reprovar();
        excluirAnexos(solicitacao);

        return new SolicitacaoResumidaDTO(repository.save(solicitacao));
    }

    @Transactional
    public SolicitacaoDetalhadaDTO atualizarSolicitacao(Long idSolicitacao, HistoricoSolicitacaoDTO dto, Long idLoja, List<ItemDTO> novosProdutos) {
        Solicitacao solicitacao = buscarSolicitacaoOuLancarExcecao(idSolicitacao, idLoja);

        validador.validarStatusVenda(solicitacao.getVenda(), solicitacao.getTipoSolicitacao().getDescricao().toLowerCase());

        StatusSolicitacao novoStatus = StatusSolicitacao.deString(dto.statusNovo());

        solicitacao.atualizar(novoStatus, dto.observacao());

        solicitacao = repository.save(solicitacao);
        gerarVendaAposTroca(solicitacao, novosProdutos);

        return new SolicitacaoDetalhadaDTO(solicitacao);
    }


    @Transactional
    public SolicitacaoResumidaDTO cancelarSolicitacao(Long idSolicitacao, Long idLoja) throws GeneralSecurityException, IOException {
        Solicitacao solicitacao = buscarSolicitacaoOuLancarExcecao(idSolicitacao, idLoja);

        solicitacao.cancelar();

        excluirAnexos(solicitacao);

        return new SolicitacaoResumidaDTO(repository.save(solicitacao));
    }


    private Solicitacao buscarSolicitacaoOuLancarExcecao(Long idSolicitacao, Long idLoja) {
        return repository.buscarCompletoPorId(idSolicitacao, idLoja)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));
    }

    private ItemVenda buscarItemVendaPorId(Long idItemVenda, List<ItemVenda> itens){
        return itens.stream()
                .filter(i -> i.getId().equals(idItemVenda))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
    }

    private void processarAnexos(List<MultipartFile> anexos, Long idSolicitacao) throws GeneralSecurityException, IOException {
        if (anexos == null || anexos.isEmpty()) {
            return;
        }

        List<ArquivoDTO> arquivos = anexos.stream()
                .filter(file -> file != null && !file.isEmpty())
                .map(file -> {
                    try {
                        return new ArquivoDTO(file.getBytes(), file.getOriginalFilename(), file.getContentType());
                    } catch (IOException e) {
                        throw new RuntimeException("Erro ao ler arquivo: " + file.getOriginalFilename(), e);
                    }
                })
                .toList();

        if (!arquivos.isEmpty()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        anexoService.processarUploadAnexos(arquivos, idSolicitacao);
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao processar anexos: " + e.getMessage(), e);
                    }
                }
            });
        }
    }

    private void excluirAnexos(Solicitacao solicitacao) throws GeneralSecurityException, IOException {
        List<String> idsAnexos = solicitacao.getAnexos().stream()
                .map(anexo -> solicitacao.getId() + "_" + anexo.getId()).toList();

        solicitacao.getAnexos().clear();

        anexoService.processarExclusaoAnexos(idsAnexos, solicitacao.getClass().getSimpleName());
    }

    private void gerarVendaAposTroca(Solicitacao solicitacao, List<ItemDTO> novosProdutos) {
        boolean solicitacaoFinalizada = solicitacao.getStatusSolicitacao() == StatusSolicitacao.CONCLUIDA;
        boolean temProdutosParaTroca = novosProdutos != null && !novosProdutos.isEmpty();

        if(solicitacaoFinalizada && temProdutosParaTroca && solicitacao.getTipoSolicitacao() == TipoSolicitacao.TROCA){
            vendaService.salvar(new RegistroVendaDTO(solicitacao, novosProdutos), solicitacao.getVenda().getLoja().getIdLoja());
        }
    }

    private void validarTipoAnexo(List<MultipartFile> anexos){
        if (anexos == null || anexos.isEmpty()) {
            return;
        }

        Set<CategoriaArquivoPermitida> categoriasPermitidas =
                Set.of(CategoriaArquivoPermitida.IMAGEM, CategoriaArquivoPermitida.VIDEO);

        anexos.forEach(arquivo -> arquivoValidador.validarOpcional(arquivo, categoriasPermitidas));
    }
}
