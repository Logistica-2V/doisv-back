package com.logistica.doisv.controllers.docs;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.registro_venda.requisicao.RegistroVendaDTO;
import com.logistica.doisv.dto.registro_venda.resposta.VendaDTO;
import com.logistica.doisv.dto.registro_venda.resposta.VendaResumidaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vendas", description = "Gerenciamento de vendas da loja")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("doisv/vendas")
public interface VendaApi {

    @Operation(summary = "Buscar todas as vendas",
            description = "Retorna uma página com todas as vendas vinculadas à loja do usuário autenticado. Suporta paginação via parâmetros `page`, `size` e `sort`.",
            operationId = "buscarTodasVendas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de vendas retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<Page<VendaResumidaDTO>> buscarTodasVendas(
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Buscar venda por ID",
            description = "Retorna os dados completos de uma venda específica pelo seu identificador.",
            operationId = "buscarVendaPorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VendaDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping(value = "/{id}")
    ResponseEntity<VendaDTO> buscarVendaPorId(
            @Parameter(description = "ID da venda", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Buscar venda do consumidor autenticado",
            description = "Retorna os dados completos da venda associada ao token do consumidor autenticado.",
            operationId = "buscarVendaPorToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VendaDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping(value = "/me")
    ResponseEntity<VendaDTO> buscarVendaPorToken(
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);

    @Operation(summary = "Criar uma nova venda",
            description = "Registra uma nova venda vinculada à loja do usuário autenticado. Envia e-mail de confirmação ao consumidor.",
            operationId = "criarVenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venda criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VendaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    ResponseEntity<VendaDTO> criarVenda(
            @Parameter(description = "Dados da venda a ser registrada", required = true)
            @Valid @RequestBody RegistroVendaDTO dto,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado) throws MessagingException;

    @Operation(summary = "Atualizar uma venda",
            description = "Atualiza os dados de uma venda existente pelo seu identificador.",
            operationId = "atualizarVenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VendaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping(value = "/{id}")
    ResponseEntity<VendaDTO> atualizarVenda(
            @Parameter(description = "ID da venda", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados da venda", required = true)
            @Valid @RequestBody RegistroVendaDTO dto,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado) throws MessagingException;

    @Operation(summary = "Desativar vendas em lote",
            description = "Desativa uma ou mais vendas informando uma lista de IDs.",
            operationId = "desativarVenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vendas desativadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PatchMapping
    ResponseEntity<Void> desativarVenda(
            @Parameter(description = "Lista de IDs das vendas a serem desativadas", required = true)
            @RequestBody List<Long> vendasIds,
            @Parameter(hidden = true) @AuthenticationPrincipal AcessoDTO usuarioLogado);
}
