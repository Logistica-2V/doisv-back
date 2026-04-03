package com.logistica.doisv.dto;

import com.logistica.doisv.util.validacao.CpfCnpj;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "LojistaAtualizacao", description = "Dados para atualização de um lojista")
public record LojistaAtualizacaoDTO(
        @Schema(description = "Nome completo do lojista", example = "João Oliveira", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome do lojista é obrigatório.") String nome,

        @Schema(description = "CPF do lojista (somente números ou formatado)", example = "12345678900", requiredMode = Schema.RequiredMode.REQUIRED)
        @CpfCnpj(message = "Informe um CPF válido.")
        @NotNull(message = "O CPF é obrigatório.") String cpf,

        @Schema(description = "E-mail do lojista", example = "joao@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.") String email,

        @Schema(description = "Status do lojista (ATIVO, INATIVO)", example = "ATIVO")
        String status) {
}

