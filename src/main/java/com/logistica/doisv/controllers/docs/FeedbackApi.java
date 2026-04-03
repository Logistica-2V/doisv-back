package com.logistica.doisv.controllers.docs;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.registro_feedback.FeedbackDTO;
import com.logistica.doisv.dto.registro_feedback.FeedbackResumidoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Feedbacks", description = "Gerenciamento de feedbacks sobre solicitações")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("doisv/feedbacks")
public interface FeedbackApi {

    @Operation(summary = "Buscar feedback por ID",
            description = "Retorna os dados completos de um feedback específico pelo seu identificador.",
            operationId = "buscarFeedbackPorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Feedback não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/{idFeedback}")
    ResponseEntity<FeedbackDTO> buscarFeedbackPorId(
            @Parameter(description = "ID do feedback", required = true, example = "1")
            @PathVariable Long idFeedback,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Buscar feedbacks por solicitação",
            description = "Retorna todos os feedbacks vinculados a uma solicitação específica.",
            operationId = "buscarFeedbacksPorSolicitacao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de feedbacks retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/solicitacoes/{idSolicitacao}")
    ResponseEntity<List<FeedbackDTO>> buscarFeedbacksPorSolicitacao(
            @Parameter(description = "ID da solicitação", required = true, example = "10")
            @PathVariable Long idSolicitacao,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Buscar feedbacks por loja",
            description = "Retorna uma página com os feedbacks resumidos de uma loja específica, filtrados por período em dias.",
            operationId = "buscarFeedbacksPorLoja")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de feedbacks retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Loja não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/lojas/{idLoja}")
    ResponseEntity<Page<FeedbackResumidoDTO>> buscarFeedbacksPorLoja(
            @Parameter(description = "ID da loja", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID idLoja,
            @Parameter(description = "Período em dias para filtrar os feedbacks", example = "180")
            @RequestParam(defaultValue = "180") Integer periodo,
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable);

    @Operation(summary = "Criar um novo feedback",
            description = "Registra um novo feedback vinculado a uma solicitação. O consumidor autenticado é automaticamente associado.",
            operationId = "criarFeedback")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feedback criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    ResponseEntity<FeedbackDTO> criarFeedback(
            @Parameter(description = "Dados do feedback a ser registrado", required = true)
            @Valid @RequestBody FeedbackDTO dto,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);
}
