package com.logistica.doisv.modules.solicitacao.validation;

import com.logistica.doisv.core.enums.*;
import com.logistica.doisv.core.exception.RegraNegocioException;
import com.logistica.doisv.modules.solicitacao.dto.CriarSolicitacaoDTO;
import com.logistica.doisv.modules.solicitacao.entity.Solicitacao;
import com.logistica.doisv.modules.venda.entity.ItemVenda;
import com.logistica.doisv.modules.venda.entity.Venda;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class SolicitacaoValidador {

    public void validarRegistroSolicitacao(Venda venda, CriarSolicitacaoDTO dto,
                                           TipoSolicitacao tipoSolicitacao,
                                           MotivoSolicitacao motivo,
                                           ItemVenda itemVenda) {
        validarStatusVenda(venda, dto.tipo());

        validarPrazoSolicitacao(venda, tipoSolicitacao);

        validarMotivoSolicitacao(motivo, tipoSolicitacao);

        validarSolicitacaoNaoExcedeCompra(dto.quantidade(), itemVenda.getQuantidade(), dto.tipo());

        validarDisponibilidadeParaSolicitacao(itemVenda, dto.quantidade());
    }

    public void validarEdicaoSolicitacao(Solicitacao solicitacao,
                                         TipoSolicitacao tipoSolicitacao,
                                         MotivoSolicitacao motivo,
                                         CriarSolicitacaoDTO dto,
                                         Long idLoja, ItemVenda itemVenda) {
        validarAprovacaoSolicitacao(solicitacao, idLoja);

        validarStatusVenda(solicitacao.getVenda(), tipoSolicitacao.getDescricao().toLowerCase());

        validarPrazoSolicitacao(solicitacao.getVenda(), tipoSolicitacao);

        validarItemVendaDiferente(solicitacao, itemVenda);

        validarMotivoSolicitacao(motivo, tipoSolicitacao);

        validarSolicitacaoNaoExcedeCompra(dto.quantidade(),
                itemVenda.getQuantidade(),
                tipoSolicitacao.getDescricao().toLowerCase());

        validarDisponibilidadeParaSolicitacao(itemVenda, dto.quantidade());
    }


    public void validarStatusVenda(Venda venda, String tipo) {
        if (venda.getStatusPedido() != StatusPedido.ENTREGUE || venda.getStatus() == Status.INATIVO) {
            throw new RegraNegocioException(String
                    .format("Não é possível realizar uma solicitação de %s com o status atual da venda", tipo));
        }
    }

    public void validarPrazoSolicitacao(Venda venda, TipoSolicitacao tipoSolicitacao) {
        int prazo = tipoSolicitacao == TipoSolicitacao.TROCA ? venda.getPrazoTroca() : venda.getPrazoDevolucao();

        LocalDate dataLimite = venda.getDataEntrega().plusDays(prazo);

        if (LocalDate.now().isAfter(dataLimite)) {
            throw new RegraNegocioException(String.format("Período para solicitar %s encerrou em %s",
                    tipoSolicitacao.getDescricao(),
                    dataLimite.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        }
    }

    public void validarSolicitacaoNaoExcedeCompra(Double quantidadeSolicitada, Double quantidadeComprada, String tipo) {
        if (quantidadeSolicitada > quantidadeComprada) {
            throw new RegraNegocioException(String
                    .format("A quantidade selecionada para %s é maior que a quantidade comprada", tipo.toLowerCase()));
        }
    }

    public void validarDisponibilidadeParaSolicitacao(ItemVenda itemVenda, Double quantidadeSolicitada){
        if(itemVenda.getStatus() == Status.INATIVO){
            throw new RegraNegocioException(
                    "Não é possível solicitar troca ou devolução para este item, pois já existe uma solicitação registrada.");
        }

        double quantidadeJaSolicitada = itemVenda.getSolicitacoes()
                .stream()
                .filter(solicitacao -> solicitacao.getStatusSolicitacao() != StatusSolicitacao.CANCELADA &&
                        solicitacao.getStatusSolicitacao() != StatusSolicitacao.REJEITADA)
                .mapToDouble(Solicitacao::getQuantidade)
                .sum();

        double quantidadeDisponivel = itemVenda.getQuantidade() - quantidadeJaSolicitada;

        if(quantidadeSolicitada > quantidadeDisponivel){
            throw new RegraNegocioException(String.format(
                    "A quantidade solicitada (%.2f) excede a quantidade disponível (%.2f) para este item.",
                    quantidadeSolicitada, quantidadeDisponivel));
        }
    }

    public void validarAprovacaoSolicitacao(Solicitacao solicitacao, Long idLoja){
        boolean validarStatusSolicitacao = solicitacao.getStatusSolicitacao() != StatusSolicitacao.PENDENTE;
        boolean validarStatus = solicitacao.getStatus() == Status.INATIVO;

        if (validarStatusSolicitacao || validarStatus){
            throw new RegraNegocioException(String.format("A solicitação de %s ID %s não pode mais ser alterada.",
                    solicitacao.getTipoSolicitacao().getDescricao().toLowerCase(),
                    solicitacao.getId()));
        }
    }
    
    private void validarItemVendaDiferente(Solicitacao solicitacao, ItemVenda itemVenda){
        if(!solicitacao.getItemVenda().equals(itemVenda)){
            throw new RegraNegocioException(String
                    .format("Não é possível alterar o produto da solicitação de %s. Caso precise trocar ou devolver " +
                                    "outro produto, cancele esta solicitação e crie uma nova.",
                    solicitacao.getTipoSolicitacao().getDescricao().toLowerCase()));
        }
    }

    private void validarMotivoSolicitacao(MotivoSolicitacao motivo, TipoSolicitacao tipoSolicitacao){
        if(!motivo.permiteTipo(tipoSolicitacao)){
            throw new RegraNegocioException(
                    "O motivo informado não é permitido para solicitação de "
                            + tipoSolicitacao.getDescricao().toLowerCase());
        }
    }
}
