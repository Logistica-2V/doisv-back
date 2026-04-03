package com.logistica.doisv.dto.registro_venda.resposta;

import com.logistica.doisv.entities.Produto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProdutoVenda", description = "Dados resumidos do produto em uma venda")
public record ProdutoVendaDTO(
        @Schema(description = "Identificador único do produto", example = "1")
        Long id,

        @Schema(description = "Descrição do produto", example = "Camiseta Básica")
        String descricao) {

    public ProdutoVendaDTO(Produto produto){
        this(produto.getIdProduto(), produto.getDescricao());
    }
}
