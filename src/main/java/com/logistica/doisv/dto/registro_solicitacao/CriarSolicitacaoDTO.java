package com.logistica.doisv.dto.registro_solicitacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CriarSolicitacaoDTO(@NotNull Long idItem,
                                  @Positive Double quantidade,
                                  @NotBlank String tipo,
                                  @NotBlank String motivo) {
}
