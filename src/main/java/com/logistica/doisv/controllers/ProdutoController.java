package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.ProdutoDTO;
import com.logistica.doisv.services.ProdutoService;
import com.logistica.doisv.services.validacao.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping("doisv/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {
    @Autowired
    private ProdutoService service;

    @Autowired
    private TokenService tokenService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        ProdutoDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> buscarTodos(Pageable pageable, @RequestHeader String Authorization) {
        AcessoDTO acessoDTO = tokenService.validarToken(Authorization);
        Page<ProdutoDTO> dto = service.buscarTodos(pageable, acessoDTO.getIdLoja());
        return ResponseEntity.ok(dto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoDTO> salvar(@Valid @RequestPart("produto") ProdutoDTO dto, @RequestPart("imagem") MultipartFile imagem,
                                             @RequestHeader String authorization) throws GeneralSecurityException, IOException {

        AcessoDTO acesso = tokenService.validarToken(authorization);
        dto = service.salvar(dto, imagem, acesso.getIdLoja());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.idProduto()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @Valid @RequestPart("produto") ProdutoDTO dto,
                                                @RequestPart("imagem") MultipartFile imagem, @RequestHeader String Authorization) throws GeneralSecurityException, IOException {
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        dto = service.atualizar(id, dto, acesso.getIdLoja(), imagem);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProdutoDTO> excluir(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
