package com.logistica.doisv.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginConsumidor", description = "Credenciais de login do consumidor")
public record ConsumidorLoginDTO(
        @Schema(description = "Serial de acesso do consumidor", example = "ABC123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O serial é obrigatório.") String serial,

        @Schema(description = "Senha do consumidor", example = "Senha@123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A senha é obrigatória.") String senha) {
}
