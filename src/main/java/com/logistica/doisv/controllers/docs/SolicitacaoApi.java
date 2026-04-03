package com.logistica.doisv.controllers.docs;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.registro_solicitacao.CriarSolicitacaoDTO;
import com.logistica.doisv.dto.registro_solicitacao.HistoricoSolicitacaoDTO;
import com.logistica.doisv.dto.registro_solicitacao.SolicitacaoDetalhadaDTO;
import com.logistica.doisv.dto.registro_solicitacao.SolicitacaoResumidaDTO;
import com.logistica.doisv.dto.registro_venda.requisicao.ItemDTO;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Tag(name = "Solicitações", description = "Gerenciamento de solicitações (trocas, devoluções)")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("doisv/solicitacoes")
public interface SolicitacaoApi {

    @Operation(summary = "Buscar todas as solicitações",
            description = "Retorna uma página com todas as solicitações vinculadas à loja do usuário autenticado. Suporta paginação via parâmetros `page`, `size` e `sort`.",
            operationId = "buscarTodasSolicitacoes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de solicitações retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<Page<SolicitacaoResumidaDTO>> buscarTodasSolicitacoes(
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Buscar solicitação por ID",
            description = "Retorna os dados detalhados de uma solicitação específica, incluindo venda, consumidor, produto, anexos, histórico e feedbacks.",
            operationId = "buscarSolicitacaoPorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SolicitacaoDetalhadaDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping(value = "/{id}")
    ResponseEntity<SolicitacaoDetalhadaDTO> buscarSolicitacaoPorId(
            @Parameter(description = "ID da solicitação", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Criar uma nova solicitação",
            description = "Registra uma nova solicitação de troca ou devolução. Os dados devem ser enviados como multipart (JSON + anexos obrigatórios). Requer permissão CONSUMIDOR.",
            operationId = "criarSolicitacao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SolicitacaoResumidaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(value = "/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<SolicitacaoResumidaDTO> criarSolicitacao(
            @Parameter(description = "Dados da solicitação em formato JSON", required = true)
            @Valid @RequestPart("solicitacao") CriarSolicitacaoDTO dto,
            @Parameter(description = "Arquivos de anexo da solicitação", required = true)
            @RequestPart("anexos") List<MultipartFile> anexos,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado) throws GeneralSecurityException, IOException;

    @Operation(summary = "Atualizar status de uma solicitação",
            description = "Registra uma atualização no histórico da solicitação, podendo incluir novos produtos associados.",
            operationId = "atualizarSolicitacao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SolicitacaoDetalhadaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(value = "/atualizar/{id}")
    ResponseEntity<SolicitacaoDetalhadaDTO> atualizarSolicitacao(
            @Parameter(description = "ID da solicitação", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados do histórico de atualização", required = true)
            @Valid @RequestPart("historico") HistoricoSolicitacaoDTO dto,
            @Parameter(description = "Lista de novos produtos a serem adicionados")
            @RequestPart(value = "novosProdutos", required = false) List<ItemDTO> novosProdutos,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Aprovar uma solicitação",
            description = "Aprova uma solicitação de troca ou devolução pendente.",
            operationId = "aprovarSolicitacao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação aprovada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SolicitacaoResumidaDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping(value = "/aprovar/{id}")
    ResponseEntity<SolicitacaoResumidaDTO> aprovarSolicitacao(
            @Parameter(description = "ID da solicitação", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Reprovar uma solicitação",
            description = "Reprova uma solicitação de troca ou devolução pendente.",
            operationId = "reprovarSolicitacao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação reprovada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SolicitacaoResumidaDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping(value = "/reprovar/{id}")
    ResponseEntity<SolicitacaoResumidaDTO> reprovarSolicitacao(
            @Parameter(description = "ID da solicitação", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado) throws GeneralSecurityException, IOException;

    @Operation(summary = "Editar uma solicitação",
            description = "Edita os dados de uma solicitação existente, substituindo anexos. Os dados devem ser enviados como multipart (JSON + anexos obrigatórios). Requer permissão CONSUMIDOR.",
            operationId = "editarSolicitacao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação editada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SolicitacaoResumidaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping(value = "/{id}")
    ResponseEntity<SolicitacaoResumidaDTO> editarSolicitacao(
            @Parameter(description = "ID da solicitação", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados da solicitação em formato JSON", required = true)
            @Valid @RequestPart("solicitacao") CriarSolicitacaoDTO dto,
            @Parameter(description = "Arquivos de anexo da solicitação", required = true)
            @RequestPart("anexos") List<MultipartFile> anexos,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado) throws GeneralSecurityException, IOException;

    @Operation(summary = "Cancelar uma solicitação",
            description = "Cancela uma solicitação de troca ou devolução existente.",
            operationId = "cancelarSolicitacao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação cancelada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SolicitacaoResumidaDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping(value = "/cancelar/{id}")
    ResponseEntity<SolicitacaoResumidaDTO> cancelarSolicitacao(
            @Parameter(description = "ID da solicitação", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado) throws GeneralSecurityException, IOException;
}
