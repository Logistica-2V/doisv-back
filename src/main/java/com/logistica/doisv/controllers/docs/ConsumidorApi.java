package com.logistica.doisv.controllers.docs;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.ConsumidorDTO;
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

@Tag(name = "Consumidores", description = "Gerenciamento de consumidores da loja")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("doisv/consumidores")
public interface ConsumidorApi {

    @Operation(summary = "Buscar todos os consumidores",
            description = "Retorna a lista de todos os consumidores vinculados à loja do usuário autenticado.",
            operationId = "buscarTodosConsumidores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de consumidores retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ConsumidorDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<ConsumidorDTO>> buscarTodosConsumidores(
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Buscar consumidor por ID",
            description = "Retorna os dados de um consumidor específico pelo seu identificador.",
            operationId = "buscarConsumidorPorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consumidor encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsumidorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Consumidor não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping(value = "/{id}")
    ResponseEntity<ConsumidorDTO> buscarConsumidorPorId(
            @Parameter(description = "ID do consumidor", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Criar um novo consumidor",
            description = "Cadastra um novo consumidor vinculado à loja do usuário autenticado.",
            operationId = "criarConsumidor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consumidor criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsumidorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    ResponseEntity<ConsumidorDTO> criarConsumidor(
            @Parameter(description = "Dados do consumidor", required = true)
            @Valid @RequestBody ConsumidorDTO dto,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Atualizar um consumidor",
            description = "Atualiza os dados de um consumidor existente.",
            operationId = "atualizarConsumidor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consumidor atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsumidorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Consumidor não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping(value = "/{id}")
    ResponseEntity<ConsumidorDTO> atualizarConsumidor(
            @Parameter(description = "ID do consumidor", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do consumidor", required = true)
            @Valid @RequestBody ConsumidorDTO dto,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Desativar consumidores em lote",
            description = "Desativa um ou mais consumidores informando uma lista de IDs.",
            operationId = "desativarConsumidor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consumidores desativados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PatchMapping
    ResponseEntity<Void> desativarConsumidor(
            @Parameter(description = "Lista de IDs dos consumidores a serem desativados", required = true)
            @RequestBody List<Long> consumidoresIds,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Deletar um consumidor",
            description = "Remove permanentemente um consumidor do sistema. Requer permissão ADMIN.",
            operationId = "deletarConsumidor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consumidor removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Consumidor não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deletarConsumidor(
            @Parameter(description = "ID do consumidor", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);
}
