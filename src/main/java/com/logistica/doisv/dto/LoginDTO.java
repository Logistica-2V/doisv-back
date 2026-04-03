package com.logistica.doisv.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginLojista", description = "Credenciais de login do lojista")
public record LoginDTO(
        @Schema(description = "E-mail do lojista", example = "lojista@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.") String email,

        @Schema(description = "Senha do lojista", example = "Senha@123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A senha é obrigatória.") String password) {
}
