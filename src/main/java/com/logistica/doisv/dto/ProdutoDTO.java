package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Loja;
import com.logistica.doisv.entities.Produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {

    private Long idProduto;
    @NotBlank
    private String descricao;
    @NotBlank
    private String unidadeMedida;
    @PositiveOrZero
    private Double preco;
    private String imagem;
    @NotBlank
    private String statusProduto;

    @NotNull
    private Loja loja;

    public ProdutoDTO(Produto produto){
        idProduto = produto.getIdProduto();
        descricao = produto.getDescricao();
        unidadeMedida = produto.getUnidadeMedida();
        preco = produto.getPreco();
        statusProduto = produto.getStatusProduto();
        loja = produto.getLoja();
        imagem = produto.getImagem();
    }
}
