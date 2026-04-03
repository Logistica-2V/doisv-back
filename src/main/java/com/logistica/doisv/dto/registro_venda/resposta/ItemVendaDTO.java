package com.logistica.doisv.dto.registro_venda.resposta;

import com.logistica.doisv.entities.ItemVenda;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ItemVenda", description = "Dados de um item de venda")
public record ItemVendaDTO(
        @Schema(description = "Identificador único do item de venda", example = "1")
        Long id,

        @Schema(description = "Preço original do produto", example = "59.90")
        BigDecimal precoOriginal,

        @Schema(description = "Preço efetivamente vendido", example = "49.90")
        BigDecimal precoVendido,

        @Schema(description = "Percentual de variação entre preço original e vendido", example = "-16.69")
        BigDecimal percentualVariacao,

        @Schema(description = "Quantidade vendida", example = "2.0")
        Double quantidade,

        @Schema(description = "Detalhes adicionais sobre o item", example = "Cor azul, tamanho M")
        String detalhes,

        @Schema(description = "Dados do produto associado")
        ProdutoVendaDTO produto) {

    public ItemVendaDTO(ItemVenda item){
        this(item.getId(), item.getPrecoOriginal(), item.getPrecoVendido(), item.getPercentualVariacao(), item.getQuantidade(),
                item.getDetalhes(), new ProdutoVendaDTO(item.getProduto()));
    }

}
