package com.logistica.doisv.services;

import com.logistica.doisv.dto.CriarSolicitacaoDTO;
import com.logistica.doisv.entities.ItemVenda;
import com.logistica.doisv.entities.Solicitacao;
import com.logistica.doisv.entities.Venda;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.entities.enums.StatusPedido;
import com.logistica.doisv.entities.enums.TipoSolicitacao;
import com.logistica.doisv.repositories.SolicitacaoRepository;
import com.logistica.doisv.repositories.VendaRepository;
import com.logistica.doisv.services.exceptions.RegraNegocioException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository repository;

    @Autowired
    private VendaRepository vendaRepository;

    @Transactional
    public void registrarSolicitacao(CriarSolicitacaoDTO dto, Long idVenda){
        Venda venda = vendaRepository.findById(idVenda).orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
        validarStatusVenda(venda, dto.tipo());

        TipoSolicitacao tipoSolicitacao = TipoSolicitacao.deString(dto.tipo());

        validarPrazoSolicitacao(venda, tipoSolicitacao);

        ItemVenda itemVenda = venda.getItensVenda().stream()
                .filter(i -> i.getId().equals(dto.idItem()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item com ID " + dto.idItem() + " não encontrado."));

        validarQuantidade(dto.quantidade(), itemVenda.getQuantidade(), dto.tipo());

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setVenda(venda);
        solicitacao.setItemVenda(itemVenda);
        solicitacao.setConsumidor(venda.getConsumidor());
        solicitacao.setTipoSolicitacao(tipoSolicitacao);
        solicitacao.setQuantidade(dto.quantidade());
        solicitacao.setMotivo(dto.motivo());
        solicitacao.setDataSolicitacao(Instant.now());

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

}
