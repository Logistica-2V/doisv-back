package com.logistica.doisv.core.enums;

import com.logistica.doisv.core.exception.ResourceNotFoundException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;

public enum MotivoSolicitacao {
    PRODUTO_COM_DEFEITO(
            "Produto com defeito",
            TipoSolicitacao.TROCA,
            TipoSolicitacao.DEVOLUCAO
    ),

    PRODUTO_DANIFICADO_ENTREGA(
            "Produto danificado na entrega",
            TipoSolicitacao.TROCA,
            TipoSolicitacao.DEVOLUCAO
    ),

    PRODUTO_DIFERENTE_DO_PEDIDO(
            "Produto diferente do pedido",
            TipoSolicitacao.TROCA,
            TipoSolicitacao.DEVOLUCAO
    ),

    PRODUTO_INCOMPLETO(
            "Produto incompleto",
            TipoSolicitacao.TROCA,
            TipoSolicitacao.DEVOLUCAO
    ),

    TAMANHO_INCORRETO(
            "Tamanho incorreto",
            TipoSolicitacao.TROCA
    ),

    COR_INCORRETA(
            "Cor incorreta",
            TipoSolicitacao.TROCA
    ),

    MODELO_INCORRETO(
            "Modelo incorreto",
            TipoSolicitacao.TROCA
    ),

    ARREPENDIMENTO_COMPRA(
            "Arrependimento da compra",
            TipoSolicitacao.DEVOLUCAO
    ),

    NAO_ATENDEU_EXPECTATIVA(
            "Não atendeu à expectativa",
            TipoSolicitacao.DEVOLUCAO
    ),

    DESCRICAO_DIVERGENTE(
            "Descrição divergente",
            TipoSolicitacao.DEVOLUCAO
    ),

    ATRASO_ENTREGA(
            "Atraso na entrega",
            TipoSolicitacao.DEVOLUCAO
    ),

    COMPRA_DUPLICADA(
            "Compra duplicada",
            TipoSolicitacao.DEVOLUCAO
    ),

    OUTRO(
            "Outro",
            TipoSolicitacao.TROCA,
            TipoSolicitacao.DEVOLUCAO
    );

    @Getter
    private final String descricao;

    private final Set<TipoSolicitacao> tiposPermitidos;

    MotivoSolicitacao(String descricao, TipoSolicitacao... tiposPermitidos){
        this.descricao = descricao;
        this.tiposPermitidos = Set.of(tiposPermitidos);
    }

    public boolean permiteTipo(TipoSolicitacao tipoSolicitacao){
        return tiposPermitidos.contains(tipoSolicitacao);
    }

    public static MotivoSolicitacao deString(String motivo){
        return Arrays.stream(values())
                .filter(m -> m.name().equalsIgnoreCase(motivo) ||
                        m.descricao.equalsIgnoreCase(motivo))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Motivo de solicitação inválido: " + motivo));
    }
}
