package com.logistica.doisv.entities.enums;

import lombok.Getter;

public enum StatusSolicitacao {
    PENDENTE("Pendente"),
    APROVADA("Aprovada"),
    EM_ANDAMENTO("Em andamento"),
    EM_TRANSITO("Em trânsito"),
    CONCLUIDA("Concluída"),
    REJEITADA("Rejeitada"),
    CANCELADA("Cancelada");

    @Getter
    private final String statusSolicitacao;

    StatusSolicitacao(String status){this.statusSolicitacao = status;}

    public boolean podeAvancarPara(StatusSolicitacao proximo) {
        return proximo.ordinal() > this.ordinal();
    }

    public static StatusSolicitacao deString(String status){
        for(StatusSolicitacao s : StatusSolicitacao.values()){
            if(s.statusSolicitacao.equalsIgnoreCase(status)){
                return s;
            }
        }
        throw new IllegalArgumentException("Status de Solicitação não localizado");
    }
}
