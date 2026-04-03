package com.logistica.doisv.controllers.docs;

import com.logistica.doisv.dto.LojaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Tag(name = "Lojas", description = "Gerenciamento de lojas do sistema")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("doisv/lojas")
public interface LojaApi {

    @Operation(summary = "Buscar todas as lojas", description = "Retorna a lista completa de todas as lojas cadastradas.",
            operationId = "buscarTodasLojas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de lojas retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LojaDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<LojaDTO>> buscarTodasLojas();

    @Operation(summary = "Buscar loja por ID", description = "Retorna os dados de uma loja específica pelo seu identificador.",
            operationId = "buscarLojaPorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loja encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LojaDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Loja não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping(value = "/{id}")
    ResponseEntity<LojaDTO> buscarLojaPorId(
            @Parameter(description = "ID da loja", required = true, example = "1")
            @PathVariable Long id);

    @Operation(summary = "Criar uma nova loja",
            description = "Cadastra uma nova loja com os dados informados e o arquivo de logo. Requer permissão MASTER.",
            operationId = "criarLoja")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Loja criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LojaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<LojaDTO> criarLoja(
            @Parameter(description = "Dados da loja em formato JSON", required = true)
            @Valid @RequestPart("loja") LojaDTO dto,
            @Parameter(description = "Arquivo de logo da loja", required = true)
            @RequestPart("logo") MultipartFile logo) throws GeneralSecurityException, IOException, MessagingException;

    @Operation(summary = "Atualizar uma loja",
            description = "Atualiza os dados de uma loja existente, incluindo o arquivo de logo.",
            operationId = "atualizarLoja")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loja atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LojaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Loja não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping(value = "/{id}")
    ResponseEntity<?> atualizarLoja(
            @Parameter(description = "ID da loja", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados da loja em formato JSON", required = true)
            @Valid @RequestPart("loja") LojaDTO dto,
            @Parameter(description = "Novo arquivo de logo da loja", required = true)
            @RequestPart("logo") MultipartFile logo) throws GeneralSecurityException, IOException;

    @Operation(summary = "Desativar uma loja",
            description = "Desativa uma loja existente alterando seu status para inativo.",
            operationId = "desativarLoja")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Loja desativada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Loja não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PatchMapping(value = "/{id}")
    ResponseEntity<Void> desativarLoja(
            @Parameter(description = "ID da loja", required = true, example = "1")
            @PathVariable Long id);

    @Operation(summary = "Deletar uma loja",
            description = "Remove permanentemente uma loja do sistema. Requer permissão MASTER.",
            operationId = "deletarLoja")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Loja removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Loja não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deletarLoja(
            @Parameter(description = "ID da loja", required = true, example = "1")
            @PathVariable Long id);
}
