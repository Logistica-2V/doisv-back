package com.logistica.doisv.dto.registro_venda.requisicao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

public record RegistroVendaDTO(@DecimalMin(value = "0.0") @Digits(integer = 6, fraction = 2) BigDecimal desconto, @NotBlank Long idConsumidor,
                                    Integer prazoTroca, Integer prazoDevolucao,
                                    String formaPagamento, @NotBlank String statusPedido, @NotBlank List<ItemDTO> itensVenda) {
}
