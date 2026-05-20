package com.logistica.doisv.modules.metrica.dto;

import com.logistica.doisv.modules.feedback.dto.FeedbackResumidoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
@Schema(name = "MetricasPrivadas", description = "Métricas privadas da loja, incluindo notas, vendas e feedbacks")
public record MetricasPrivadasDTO(
        @Schema(description = "Nota média por tipo de feedback (ex: ELOGIO, RECLAMACAO, SUGESTAO)",
                example = "{\"ELOGIO\": 4.5, \"RECLAMACAO\": 2.0, \"SUGESTAO\": 3.8}")
        Map<String, BigDecimal> notaMedia,

        @Schema(description = "Quantidade de solicitações únicas que receberam feedbacks no período", example = "150")
        Integer totalSolicitacoesAvaliadas,

        @Schema(description = "Quantidade de feedbacks agrupados por tipo",
                example = "{\"ELOGIO\": 50, \"RECLAMACAO\": 10, \"SUGESTAO\": 20}")
        Map<String, Integer> quantidadeFeedbackPorTipo,

        @Schema(description = "Percentual de feedbacks por tipo",
                example = "{\"ELOGIO\": 62.50, \"RECLAMACAO\": 12.50, \"SUGESTAO\": 25.00}")
        Map<String, BigDecimal> percentualPorTipo,

        @Schema(description = "Avaliações resumidas agrupadas por tipo de feedback")
        Map<String, List<FeedbackResumidoDTO>> avaliacoes){
}
