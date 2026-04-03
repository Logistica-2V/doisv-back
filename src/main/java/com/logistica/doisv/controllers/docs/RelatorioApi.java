package com.logistica.doisv.controllers.docs;

import com.logistica.doisv.dto.AcessoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Relatórios", description = "Exportação de relatórios em formato Excel")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("doisv/relatorios")
public interface RelatorioApi {

    @Operation(summary = "Exportar relatório em Excel",
            description = "Gera e retorna um arquivo Excel (.xlsx) do relatório solicitado. "
                    + "Os tipos disponíveis são: consumidores, vendas, solicitacoes e produtos. "
                    + "O relatório é filtrado pela loja do usuário autenticado.",
            operationId = "exportarExcel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo Excel gerado com sucesso",
                    content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
            @ApiResponse(responseCode = "400", description = "Tipo de relatório inválido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/{relatorio}/excel")
    ResponseEntity<byte[]> exportarExcel(
            @Parameter(description = "Tipo do relatório a ser exportado (consumidores, vendas, solicitacoes, produtos)",
                    required = true, example = "vendas")
            @PathVariable String relatorio,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);
}
