package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ProdutoDTO(Long idProduto, @NotBlank String descricao, @NotBlank String unidadeMedida, @PositiveOrZero Double preco, String imagem) {

    public ProdutoDTO(Produto produto){
        this(produto.getIdProduto(), produto.getDescricao(), produto.getUnidadeMedida(), produto.getPreco(), produto.getImagem());
    }
}
