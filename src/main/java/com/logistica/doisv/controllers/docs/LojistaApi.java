package com.logistica.doisv.controllers.docs;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.LojistaAtualizacaoDTO;
import com.logistica.doisv.dto.LojistaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Lojistas", description = "Gerenciamento de lojistas do sistema")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("doisv/lojistas")
public interface LojistaApi {

    @Operation(summary = "Buscar lojista por ID",
            description = "Retorna os dados de um lojista específico pelo seu identificador.",
            operationId = "buscarLojistaPorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lojista encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LojistaDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lojista não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping(value = "/{id}")
    ResponseEntity<LojistaDTO> buscarLojistaPorId(
            @Parameter(description = "ID do lojista", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Buscar perfil do lojista autenticado",
            description = "Retorna os dados do lojista associado ao token JWT informado. Requer permissão LOJISTA.",
            operationId = "buscarLojistaPorToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil do lojista retornado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LojistaDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping(value = "/profile")
    ResponseEntity<LojistaDTO> buscarLojistaPorToken(
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Buscar todos os lojistas da loja",
            description = "Retorna a lista de todos os lojistas vinculados à loja do usuário autenticado.",
            operationId = "buscarTodosLojistasPorLoja")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de lojistas retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LojistaDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<LojistaDTO>> buscarTodosLojistasPorLoja(
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Criar um novo lojista",
            description = "Cadastra um novo lojista vinculado à loja do usuário autenticado. Requer permissão ADMIN.",
            operationId = "criarLojista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lojista criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LojistaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    ResponseEntity<LojistaDTO> criarLojista(
            @Parameter(description = "Dados do lojista", required = true)
            @Valid @RequestBody LojistaDTO dto,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Atualizar um lojista",
            description = "Atualiza os dados de um lojista existente. Requer permissão ADMIN.",
            operationId = "atualizarLojista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lojista atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LojistaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lojista não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping(value = "/{id}")
    ResponseEntity<LojistaDTO> atualizarLojista(
            @Parameter(description = "ID do lojista", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do lojista", required = true)
            @Valid @RequestBody LojistaAtualizacaoDTO dto,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Desativar lojistas em lote",
            description = "Desativa um ou mais lojistas informando uma lista de IDs.",
            operationId = "desativarLojistas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lojistas desativados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PatchMapping
    ResponseEntity<Void> desativarLojistas(
            @Parameter(description = "Lista de IDs dos lojistas a serem desativados", required = true)
            @RequestBody List<Long> lojistasIds,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Deletar um lojista",
            description = "Remove permanentemente um lojista do sistema. Requer permissão ADMIN.",
            operationId = "deletarLojista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lojista removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lojista não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deletarLojista(
            @Parameter(description = "ID do lojista", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);
}
