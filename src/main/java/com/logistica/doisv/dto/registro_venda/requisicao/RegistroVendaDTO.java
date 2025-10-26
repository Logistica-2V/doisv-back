package com.logistica.doisv.dto.registro_venda.requisicao;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record RegistroVendaDTO(@DecimalMin(value = "0.0") @Digits(integer = 6, fraction = 2) BigDecimal desconto, @NotNull Long idConsumidor,
                               @PositiveOrZero Integer prazoTroca, @PositiveOrZero Integer prazoDevolucao,
                               String formaPagamento, @NotBlank String statusPedido, @NotEmpty List<ItemDTO> itensVenda) {
}
