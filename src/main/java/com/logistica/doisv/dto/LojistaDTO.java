package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Lojista;
import com.logistica.doisv.util.validacao.CpfCnpj;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Lojista", description = "Dados de um lojista")
public record LojistaDTO(
        @Schema(description = "Identificador único do lojista", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Nome completo do lojista", example = "João Oliveira", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome do lojista é obrigatório.") String nome,

        @Schema(description = "CPF do lojista (somente números ou formatado)", example = "12345678900", requiredMode = Schema.RequiredMode.REQUIRED)
        @CpfCnpj(message = "Informe um CPF válido.")
        @NotNull(message = "O CPF é obrigatório.") String cpf,

        @Schema(description = "E-mail do lojista", example = "joao@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.") String email,

        @Schema(description = "Senha do lojista", example = "Senha@123", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotBlank(message = "A senha é obrigatória.") String password,

        @Schema(description = "ID da loja vinculada ao lojista", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long idLoja,

        @Schema(description = "Status do lojista (ATIVO, INATIVO)", example = "ATIVO", accessMode = Schema.AccessMode.READ_ONLY)
        String status) {

    public LojistaDTO {
        cpf = cpf != null ? cpf.replaceAll("[^0-9A-Za-z]", "").toUpperCase() : null;
    }

        public LojistaDTO(Lojista lojista){
            this(lojista.getIdLojista(), lojista.getNome(), lojista.getCpf(), lojista.getEmail(), "", lojista.getLoja().getIdLoja(),
                        lojista.getStatus().toString());
        }
}


