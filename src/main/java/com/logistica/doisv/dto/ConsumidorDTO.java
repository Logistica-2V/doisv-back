package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Consumidor;
import com.logistica.doisv.util.validacao.CpfCnpj;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Consumidor", description = "Dados de um consumidor")
public record ConsumidorDTO(
        @Schema(description = "Identificador único do consumidor", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long idConsumidor,

        @Schema(description = "Nome completo do consumidor", example = "Maria da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome do consumidor é obrigatório.") String nome,

        @Schema(description = "CPF ou CNPJ do consumidor (somente números ou formatado)", example = "12345678900")
        @CpfCnpj(message = "Informe um CPF/CNPJ válido.") String cpf_cnpj,

        @Schema(description = "E-mail do consumidor", example = "maria@email.com")
        @Email(message = "Informe um e-mail válido.") String email,

        @Schema(description = "Número de celular do consumidor", example = "11999998888", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O celular é obrigatório.") String celular,

        @Schema(description = "Número de telefone fixo do consumidor", example = "1133334444")
        String telefone,

        @Schema(description = "Endereço completo do consumidor", example = "Rua das Flores, 123 - São Paulo/SP")
        String endereco,

        @Schema(description = "Status do consumidor (ATIVO, INATIVO)", example = "ATIVO", accessMode = Schema.AccessMode.READ_ONLY)
        String status) {

    public ConsumidorDTO {
        cpf_cnpj = cpf_cnpj != null ? cpf_cnpj.replaceAll("[^0-9A-Za-z]", "").toUpperCase() : null;
    }

    public ConsumidorDTO(Consumidor consumidor){
        this(consumidor.getIdConsumidor(), consumidor.getNome(), consumidor.getCpf_cnpj(), consumidor.getEmail(), consumidor.getCelular(), consumidor.getTelefone()
                , consumidor.getEndereco(), consumidor.getStatus().toString());
    }
}

