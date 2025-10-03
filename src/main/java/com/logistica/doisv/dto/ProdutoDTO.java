package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Produto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProdutoDTO(Long idProduto, @NotBlank String descricao, @NotBlank String unidadeMedida,
                         @DecimalMin(value = "0.0") @Digits(integer = 6, fraction = 2) BigDecimal preco, String imagem, String status) {

    public ProdutoDTO(Produto produto){
        this(produto.getIdProduto(), produto.getDescricao(), produto.getUnidadeMedida(), produto.getPreco(), produto.getImagem(), produto.getStatus().toString());
    }
}
