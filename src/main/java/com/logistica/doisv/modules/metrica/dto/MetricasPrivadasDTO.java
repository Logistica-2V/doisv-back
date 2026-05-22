package com.logistica.doisv.modules.metrica.dto;

import com.logistica.doisv.modules.solicitacao.dto.SolicitacaoResumidaDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
@Schema(name = "MetricasPrivadas", description = "Métricas privadas da loja, incluindo notas, vendas e feedbacks")
public record MetricasPrivadasDTO(
        @Schema(description = "Quantidade de solicitações únicas que receberam feedbacks no período", example = "150")
        Integer totalSolicitacoesAvaliadas,

        @Schema(description = "Nota média por tipo de feedback (ex: ELOGIO, RECLAMACAO, SUGESTAO)",
                example = "{\"ELOGIO\": 4.5, \"RECLAMACAO\": 2.0, \"SUGESTAO\": 3.8}")
        Map<String, BigDecimal> notaMedia,

        @Schema(description = "Percentual de solicitações concluídas em relação ao total de solicitações do período", example = "72.50")
        BigDecimal taxaConclusaoGeral,

        @Schema(description = "Quantidade de solicitações agrupadas por status",
                example = "{\"pendente\": 4, \"aprovada\": 2, \"em_andamento\": 1, \"em_transito\": 0, \"concluida\": 8, \"cancelada\": 1, \"rejeitada\": 0}")
        Map<String, Integer> quantidadeSolicitacoesPorStatus,

        @Schema(description = "Quantidade de solicitações agrupadas por motivo",
                example = "{\"produto com defeito\": 5, \"produto diferente do pedido\": 2, \"arrependimento da compra\": 1}")
        Map<String, Integer> quantidadeSolicitacoesPorMotivo,

        @Schema(description = "Cinco produtos com maior quantidade de solicitações no período")
        List<ProdutoDashboardDTO> top5ProdutosComMaisSolicitacoes,

        @Schema(description = "Solicitações pendentes no período")
        List<SolicitacaoResumidaDTO> solicitacoesPendentes,

        @Schema(description = "Quantidade de feedbacks por nota agrupada por tipo de feedback",
                example = "{\"loja\": {\"1\": 0, \"2\": 1, \"3\": 2, \"4\": 5, \"5\": 10}, \"solicitacao\": {\"1\": 1, \"2\": 0, \"3\": 3, \"4\": 4, \"5\": 8}}")
        Map<String, Map<Integer, Integer>> quantidadeFeedbacksPorNota){
}
