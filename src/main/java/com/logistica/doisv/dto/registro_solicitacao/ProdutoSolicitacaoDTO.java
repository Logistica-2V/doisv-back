package com.logistica.doisv.dto.registro_solicitacao;

import com.logistica.doisv.entities.ItemVenda;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ProdutoSolicitacao", description = "Dados do produto associado a uma solicitação")
public record ProdutoSolicitacaoDTO(
        @Schema(description = "ID do item de venda", example = "1")
        Long idItemVenda,

        @Schema(description = "ID do produto", example = "5")
        Long idProduto,

        @Schema(description = "Descrição do produto", example = "Camiseta Básica")
        String descricao,

        @Schema(description = "Unidade de medida do produto", example = "UN")
        String unidadeMedida,

        @Schema(description = "Preço vendido do produto", example = "49.90")
        BigDecimal precoVendido,

        @Schema(description = "URL da imagem do produto", example = "https://drive.google.com/imagem.jpg")
        String imagem) {

    public ProdutoSolicitacaoDTO(ItemVenda itemVenda){
        this(itemVenda.getId(), itemVenda.getProduto().getIdProduto(), itemVenda.getProduto().getDescricao(),
                itemVenda.getProduto().getUnidadeMedida(), itemVenda.getPrecoVendido(),
                itemVenda.getProduto().getImagem());
    }
}
