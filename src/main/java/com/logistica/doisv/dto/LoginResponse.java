package com.logistica.doisv.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse", description = "Resposta de autenticação contendo o token JWT")
public record LoginResponse(
        @Schema(description = "Token JWT para autenticação nas requisições subsequentes",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token) {}
