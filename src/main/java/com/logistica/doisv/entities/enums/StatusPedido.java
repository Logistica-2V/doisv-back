package com.logistica.doisv.entities.enums;

public enum StatusPedido {
    ENTREGUE("Entregue"),
    EM_ANDAMENTO("Em Andamento"),
    CANCELADA("Cancelada");

    private String statusPedido;

    StatusPedido(String status){
        this.statusPedido = status;
    }

    public static StatusPedido converterParaString(String status){
        for(StatusPedido pedido : StatusPedido.values()){
            if (pedido.statusPedido.equalsIgnoreCase(status))
                return pedido;
        }
        throw new IllegalArgumentException("Status não localizado");
    }
}
