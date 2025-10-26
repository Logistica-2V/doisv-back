package com.logistica.doisv.dto.registro_venda.requisicao;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ItemDTO(@NotNull Long idProduto, @Positive Double quantidade, String detalhe,
                      @DecimalMin(value = "0.0") @Digits(integer = 6, fraction = 2) BigDecimal valorVendido) {
}
