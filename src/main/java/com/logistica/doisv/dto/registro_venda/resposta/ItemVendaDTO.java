package com.logistica.doisv.dto.registro_venda.resposta;

import java.math.BigDecimal;

public record ItemVendaDTO(Long id, BigDecimal precoOriginal, BigDecimal precoVendido, Double percentualVariacao, Double quantidade, String detalhes,
                           ProdutoVendaDTO produto) {
}
