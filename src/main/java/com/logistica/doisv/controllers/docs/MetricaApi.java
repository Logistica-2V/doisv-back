package com.logistica.doisv.controllers.docs;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.MetricasPrivadasDTO;
import com.logistica.doisv.dto.MetricasPublicasLojaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Tag(name = "Métricas", description = "Consulta de métricas e indicadores das lojas")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("doisv/metricas")
public interface MetricaApi {

    @Operation(summary = "Buscar métricas privadas",
            description = "Retorna as métricas privadas da loja do usuário autenticado, incluindo nota média, total de vendas, "
                    + "quantidade de feedbacks por tipo, percentual por tipo e avaliações resumidas. Filtradas por período em dias.",
            operationId = "buscarMetricasPrivadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas privadas retornadas com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MetricasPrivadasDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/privadas")
    ResponseEntity<MetricasPrivadasDTO> buscarMetricasPrivadas(
            @Parameter(description = "Período em dias para filtrar as métricas", example = "365")
            @RequestParam(defaultValue = "365") Integer periodo,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Buscar métricas públicas",
            description = "Retorna uma página com as métricas públicas de todas as lojas, incluindo nome, logo, segmento, "
                    + "total de avaliações e nota média. Suporta paginação e filtragem por período em dias.",
            operationId = "buscarMetricasPublicas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de métricas públicas retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/publicas")
    ResponseEntity<Page<MetricasPublicasLojaDTO>> buscarMetricasPublicas(
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable,
            @Parameter(description = "Período em dias para filtrar as métricas", example = "180")
            @RequestParam(defaultValue = "180") Integer periodo);

    @Operation(summary = "Buscar quantidade de solicitações por status",
            description = "Retorna um mapa com a quantidade de solicitações agrupadas por status da loja do usuário autenticado, "
                    + "filtradas por período em dias.",
            operationId = "buscarQuantidadeSolicitacoesPorStatus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade de solicitações por status retornada com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/solicitacoes/por-status")
    ResponseEntity<Map<String, Integer>> buscarQuantidadeSolicitacoesPorStatus(
            @Parameter(description = "Período em dias para filtrar as solicitações", example = "365")
            @RequestParam(value = "periodo", defaultValue = "365") Integer periodo,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);
}
