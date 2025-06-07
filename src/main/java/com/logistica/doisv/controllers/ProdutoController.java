package com.logistica.doisv.controllers;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.ProdutoDTO;
import com.logistica.doisv.services.ProdutoService;
import com.logistica.doisv.services.validacao.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Controller
@RequestMapping("doisv/produtos")
@CrossOrigin(origins = "*")
@Tag(name = "Produto", description = "Manter produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService service;

    @Autowired
    private TokenService tokenService;

    @GetMapping(value = "/{id}")
    @Operation(summary = "Buscar por ID", description = "Rota responsável por buscar produtos pelo seu ID")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
            ProdutoDTO dto = service.buscarPorId(id);
            return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Buscar todos produtos", description = "Rota responsável por retornar todos produtos.")
    public ResponseEntity<Page<ProdutoDTO>> buscarTodos(Pageable pageable, @RequestHeader String Authorization){
        AcessoDTO acessoDTO = tokenService.validarToken(Authorization);
        Page<ProdutoDTO> dto = service.buscarTodos(pageable, acessoDTO.getIdLoja());
        return ResponseEntity.ok(dto);
    }

    @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cadastrar produto", description = "Rota responsável por cadastrar um produto.")
    public ResponseEntity<ProdutoDTO> salvar(@Valid @RequestPart("produto") ProdutoDTO dto,@RequestPart("imagem") MultipartFile imagem, 
                                            @RequestHeader String Authorization) throws GeneralSecurityException, IOException{

        AcessoDTO acesso = tokenService.validarToken(Authorization);
        dto.getLoja().setIdLoja(acesso.getIdLoja());
        dto = service.salvar(dto, imagem);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getIdProduto()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualizar produto", description = "Rota responsável por atualizar um produto através do seu ID.")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @Valid @RequestPart("produto") ProdutoDTO dto,
                                                @RequestPart("imagem") MultipartFile imagem, @RequestHeader String Authorization) throws GeneralSecurityException, IOException{
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        dto = service.atualizar(id, dto, acesso.getIdLoja(), imagem);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Excluir produto", description = "Rota responsável por excluir um produto através do seu ID.")
    public ResponseEntity<ProdutoDTO> excluir(@PathVariable Long id){
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
