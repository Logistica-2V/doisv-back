package com.logistica.doisv.controllers.docs;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.ProdutoDTO;
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

@Tag(name = "Produtos", description = "Gerenciamento de produtos da loja")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("doisv/produtos")
public interface ProdutoApi {

    @Operation(summary = "Buscar produto por ID",
            description = "Retorna os dados de um produto específico pelo seu identificador.",
            operationId = "buscarProdutoPorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping(value = "/{id}")
    ResponseEntity<ProdutoDTO> buscarProdutoPorId(
            @Parameter(description = "ID do produto", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Buscar todos os produtos",
            description = "Retorna uma página com todos os produtos vinculados à loja do usuário autenticado. Suporta paginação via parâmetros `page`, `size` e `sort`.",
            operationId = "buscarTodosProdutos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de produtos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<Page<ProdutoDTO>> buscarTodosProdutos(
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Criar um novo produto",
            description = "Cadastra um novo produto vinculado à loja do usuário autenticado. Os dados do produto devem ser enviados como multipart (JSON + imagem opcional). Requer permissão LOJISTA.",
            operationId = "criarProduto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ProdutoDTO> criarProduto(
            @Parameter(description = "Dados do produto em formato JSON", required = true)
            @Valid @RequestPart("produto") ProdutoDTO dto,
            @Parameter(description = "Arquivo de imagem do produto")
            @RequestPart(value = "imagem", required = false) MultipartFile imagem,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado) throws GeneralSecurityException, IOException;

    @Operation(summary = "Importar produtos via CSV",
            description = "Importa uma lista de produtos a partir de um arquivo CSV. Requer permissão LOJISTA.",
            operationId = "importarProdutos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produtos importados com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProdutoDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Arquivo CSV inválido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<List<ProdutoDTO>> importarProdutos(
            @Parameter(description = "Arquivo CSV contendo os produtos a serem importados", required = true)
            @RequestPart(value = "produtosCsv") MultipartFile produtosCsv,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Atualizar um produto",
            description = "Atualiza os dados de um produto existente, incluindo a imagem. Requer permissão LOJISTA.",
            operationId = "atualizarProduto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping(value = "/{id}")
    ResponseEntity<ProdutoDTO> atualizarProduto(
            @Parameter(description = "ID do produto", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do produto em formato JSON", required = true)
            @Valid @RequestPart("produto") ProdutoDTO dto,
            @Parameter(description = "Novo arquivo de imagem do produto")
            @RequestPart(value = "imagem", required = false) MultipartFile imagem,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado) throws GeneralSecurityException, IOException;

    @Operation(summary = "Desativar produtos em lote",
            description = "Desativa um ou mais produtos informando uma lista de IDs. Requer permissão LOJISTA.",
            operationId = "desativarProduto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produtos desativados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PatchMapping
    ResponseEntity<Void> desativarProduto(
            @Parameter(description = "Lista de IDs dos produtos a serem desativados", required = true)
            @RequestBody List<Long> produtosIds,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Deletar um produto",
            description = "Remove permanentemente um produto do sistema. Requer permissão ADMIN.",
            operationId = "deletarProduto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deletarProduto(
            @Parameter(description = "ID do produto", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);
}
