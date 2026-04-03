package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.math.BigDecimal;

@Schema(name = "Produto", description = "Dados de um produto")
@Builder
public record ProdutoDTO(
        @Schema(description = "Identificador único do produto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long idProduto,

        @Schema(description = "Descrição do produto", example = "Camiseta Polo G", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A descrição do produto é obrigatória.") String descricao,

        @Schema(description = "Unidade de medida do produto", example = "UN", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A unidade de medida é obrigatória.") String unidadeMedida,

        @Schema(description = "Preço unitário do produto", example = "49.90")
        @DecimalMin(value = "0.0", message = "O preço deve ser maior ou igual a 0,00.")
        @Digits(integer = 6, fraction = 2, message = "O preço deve ter no máximo 6 dígitos inteiros e 2 casas decimais.")
        BigDecimal preco,

        @Schema(description = "URL da imagem do produto armazenada no Google Drive", example = "https://drive.google.com/uc?id=abc123")
        String imagem,

        @Schema(description = "Status do produto (ATIVO, INATIVO)", example = "ATIVO", accessMode = Schema.AccessMode.READ_ONLY)
        String status) {

    public ProdutoDTO(Produto produto){
        this(produto.getIdProduto(), produto.getDescricao(), produto.getUnidadeMedida(), produto.getPreco(),
                produto.getImagem(), produto.getStatus().toString());
    }
}
