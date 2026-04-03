package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Loja;
import com.logistica.doisv.util.validacao.CpfCnpj;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Loja", description = "Dados de uma loja")
public record LojaDTO (
        @Schema(description = "Identificador único da loja", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long idLoja,

        @Schema(description = "Nome da loja", example = "Loja Exemplo LTDA", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome da loja é obrigatório.") String nome,

        @Schema(description = "CNPJ da loja (somente números ou formatado)", example = "12345678000199", requiredMode = Schema.RequiredMode.REQUIRED)
        @CpfCnpj(message = "Informe um CNPJ válido.")
        @NotNull(message = "O CNPJ é obrigatório.") String cnpj,

        @Schema(description = "Segmento de atuação da loja", example = "Vestuário", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O segmento da loja é obrigatório.") String segmento,

        @Schema(description = "URL do logo da loja armazenado no Google Drive", example = "https://drive.google.com/uc?id=abc123")
        String logo,

        @Schema(description = "E-mail de contato da loja", example = "contato@lojaexemplo.com.br", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email(message = "Informe um e-mail válido.")
        @NotBlank(message = "O e-mail da loja é obrigatório.") String email,

        @Schema(description = "Status da loja (ATIVO, INATIVO)", example = "ATIVO", accessMode = Schema.AccessMode.READ_ONLY)
        String status,

        @Schema(description = "Identificador público da loja", example = "f47ac10b-58cc", accessMode = Schema.AccessMode.READ_ONLY)
        String idPublico){

    public LojaDTO {
        cnpj = cnpj.replaceAll("[^0-9A-Za-z]", "").toUpperCase();
    }

    public LojaDTO(Loja loja) {
        this(loja.getIdLoja(), loja.getNome(), loja.getCnpj(), loja.getSegmento(), loja.getLogo(), loja.getEmail(),
                loja.getStatus().toString(), String.valueOf(loja.getIdPublico()));
    }
}

