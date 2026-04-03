package com.logistica.doisv.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Schema(name = "MetricasPublicasLoja", description = "Métricas públicas de uma loja para ranking e exibição")
public record MetricasPublicasLojaDTO(
        @Schema(description = "Identificador único da loja", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID idLoja,

        @Schema(description = "Nome da loja", example = "Loja Exemplo LTDA")
        String nomeLoja,

        @Schema(description = "URL do logotipo da loja", example = "https://drive.google.com/logo.png")
        String logo,

        @Schema(description = "Segmento de atuação da loja", example = "Eletrônicos")
        String segmento,

        @Schema(description = "Total de avaliações recebidas no período", example = "85")
        Integer totalAvaliacoes,

        @Schema(description = "Nota média por tipo de feedback",
                example = "{\"ELOGIO\": 4.5, \"RECLAMACAO\": 2.0, \"SUGESTAO\": 3.8}")
        Map<String, BigDecimal> notaMedia) {
}
