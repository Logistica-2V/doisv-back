package com.logistica.doisv.dto.registro_venda.requisicao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(name = "ItemVendaRequisicao", description = "Dados de um item para registro de venda")
public record ItemDTO(
        @Schema(description = "ID do produto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O ID do produto é obrigatório.") Long idProduto,

        @Schema(description = "Quantidade do produto", example = "2.0")
        @Positive(message = "A quantidade deve ser maior que zero.") Double quantidade,

        @Schema(description = "Detalhes adicionais sobre o item", example = "Cor azul, tamanho M")
        String detalhe,

        @Schema(description = "Valor vendido do item", example = "49.90")
        @DecimalMin(value = "0.0", message = "O valor vendido deve ser maior ou igual a 0,00.")
        @Digits(integer = 6, fraction = 2, message = "O valor vendido deve ter no máximo 6 dígitos inteiros e 2 casas decimais.")
        BigDecimal valorVendido,

        @Schema(description = "ID do item de venda (somente para atualização)", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
        @Null
        Long idItemVenda) {
}
