package com.logistica.doisv.modules.metrica.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProdutoDashboard", description = "Dados de um produto exibido no dashboard de métricas privadas")
public record ProdutoDashboardDTO(
        @Schema(description = "Nome do produto", example = "Camiseta Polo G")
        String nomeProduto,

        @Schema(description = "Quantidade de solicitações de troca do produto no período", example = "6")
        Integer quantidadeTrocas,

        @Schema(description = "Quantidade de solicitações de devolução do produto no período", example = "3")
        Integer quantidadeDevolucoes,

        @Schema(description = "Total de solicitações do produto no período", example = "9")
        Integer totalSolicitacoes,

        @Schema(description = "Nível de atenção do produto conforme o total de solicitações", example = "Alto")
        String nivelAtencao) {
}
