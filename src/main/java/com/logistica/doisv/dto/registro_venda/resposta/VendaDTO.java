package com.logistica.doisv.dto.registro_venda.resposta;

import java.math.BigDecimal;
import java.util.List;

public record VendaDTO (Long idVenda, String serial, BigDecimal precoTotal, String statusPedido, BigDecimal desconto, LojaVendaDTO loja,
                        ConsumidorVendaDTO consumidor, String status, List<ItemVendaDTO> itens) {
}
