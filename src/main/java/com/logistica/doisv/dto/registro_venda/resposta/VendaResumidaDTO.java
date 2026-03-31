package com.logistica.doisv.dto.registro_venda.resposta;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public interface VendaResumidaDTO {
    Long getIdVenda();
    String getNomeConsumidor();
    String getDataCriacao();
    BigDecimal getPrecoTotal();
    String getFormaPagamento();

    @Value("#{target.statusPedido.statusPedido}")
    String getStatusPedido();

    @Value("#{target.status.statusItem}")
    String getStatus();
}
