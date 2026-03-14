package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.ProdutoDTO;
import com.logistica.doisv.services.ProdutoService;
import com.logistica.doisv.services.validacao.TokenService;
import com.logistica.doisv.util.validacao.ArquivoValidador;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("doisv/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService service;

    @Autowired
    private TokenService tokenService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable Long id, @RequestHeader String Authorization) {
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        ProdutoDTO dto = service.buscarPorId(id, acesso.getIdLoja());
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> buscarTodosProdutos(Pageable pageable, @RequestHeader String Authorization) {
        AcessoDTO acessoDTO = tokenService.validarToken(Authorization);
        Page<ProdutoDTO> dto = service.buscarTodos(pageable, acessoDTO.getIdLoja());
        return ResponseEntity.ok(dto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoDTO> criarProduto(@Valid @RequestPart("produto") ProdutoDTO dto, @RequestPart("imagem") MultipartFile imagem,
                                             @RequestHeader String authorization) throws GeneralSecurityException, IOException {

        AcessoDTO acesso = tokenService.validarToken(authorization);
        dto = service.salvar(dto, imagem, acesso.getIdLoja());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.idProduto()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ProdutoDTO>> importarProdutos(@RequestPart(value = "produtosCsv") MultipartFile produtosCsv,
                                                             @RequestHeader String Authorization){
        AcessoDTO acessoDTO = tokenService.validarToken(Authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.importarProdutos(produtosCsv, acessoDTO.getIdLoja()));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Long id, @Valid @RequestPart("produto") ProdutoDTO dto,
                                                @RequestPart("imagem") MultipartFile imagem, @RequestHeader String Authorization) throws GeneralSecurityException, IOException {
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        dto = service.atualizar(id, dto, acesso.getIdLoja(), imagem);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping
    public ResponseEntity<Void> desativarProduto(@RequestBody List<Long> produtosIds, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        service.inativar(produtosIds, acesso.getIdLoja());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id, @RequestHeader String Authorization) {
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        service.remover(id, acesso.getIdLoja());
        return ResponseEntity.noContent().build();
    }
}
