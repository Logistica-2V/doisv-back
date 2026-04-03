package com.logistica.doisv.dto.registro_solicitacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "CriarSolicitacao", description = "Dados para criação de uma solicitação de troca ou devolução")
public record CriarSolicitacaoDTO(
        @Schema(description = "ID do item de venda relacionado à solicitação", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O ID do item é obrigatório.") Long idItem,

        @Schema(description = "Quantidade solicitada para troca/devolução", example = "2.0")
        @Positive(message = "A quantidade deve ser maior que zero.") Double quantidade,

        @Schema(description = "Tipo da solicitação (TROCA, DEVOLUCAO)", example = "TROCA", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O tipo da solicitação é obrigatório.") String tipo,

        @Schema(description = "Motivo da solicitação", example = "Produto com defeito de fabricação", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O motivo da solicitação é obrigatório.") String motivo) {
}
