package com.logistica.doisv.util.validacao;

import com.logistica.doisv.dto.registro_solicitacao.CriarSolicitacaoDTO;
import com.logistica.doisv.entities.ItemVenda;
import com.logistica.doisv.entities.Solicitacao;
import com.logistica.doisv.entities.Venda;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.entities.enums.StatusPedido;
import com.logistica.doisv.entities.enums.StatusSolicitacao;
import com.logistica.doisv.entities.enums.TipoSolicitacao;
import com.logistica.doisv.services.exceptions.RegraNegocioException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class SolicitacaoValidador {

    public void validarRegistroSolicitacao(Venda venda, CriarSolicitacaoDTO dto,
                                           TipoSolicitacao tipoSolicitacao,
                                           ItemVenda itemVenda) {
        validarStatusVenda(venda, dto.tipo());
        validarPrazoSolicitacao(venda, tipoSolicitacao);
        validarQuantidadeItemVenda(dto.quantidade(), itemVenda.getQuantidade(), dto.tipo());
    }

    public void validarEdicaoSolicitacao(Solicitacao solicitacao, CriarSolicitacaoDTO dto,
                                         Long idLoja, ItemVenda itemVenda) {
        validarAprovacaoSolicitacao(solicitacao, idLoja);
        validarStatusVenda(solicitacao.getVenda(), solicitacao.getTipoSolicitacao().getDescricao().toLowerCase());
        validarPrazoSolicitacao(solicitacao.getVenda(), solicitacao.getTipoSolicitacao());
        validarItemVendaDiferente(solicitacao, itemVenda);
        validarQuantidadeItemVenda(dto.quantidade(),
                itemVenda.getQuantidade(),
                solicitacao.getTipoSolicitacao().getDescricao().toLowerCase());
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

    public void validarQuantidadeItemVenda(Double quantidadeSolicitada, Double quantidadeComprada, String tipo) {
        if (quantidadeSolicitada > quantidadeComprada) {
            throw new RegraNegocioException(String
                    .format("A quantidade selecionada para %s é maior que a quantidade comprada", tipo.toLowerCase()));
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
}
