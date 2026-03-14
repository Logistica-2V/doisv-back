package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.LojistaDTO;
import com.logistica.doisv.services.LojistaService;
import com.logistica.doisv.services.validacao.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("doisv/lojistas")
public class LojistaController {
    @Autowired
    private LojistaService lojistaService;

    @Autowired
    private TokenService tokenService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<LojistaDTO> buscarLojistaPorId(@PathVariable Long id, @RequestHeader String Authorization){
        AcessoDTO acessoDTO = tokenService.validarToken(Authorization);
        return ResponseEntity.ok(lojistaService.buscarPorId(id, acessoDTO.getIdLoja()));
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<LojistaDTO> buscarLojistaPorToken(@RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        return ResponseEntity.ok(lojistaService.buscarPorId(acesso.getIdLojista(), acesso.getIdLoja()));
    }

//    @GetMapping
//    public ResponseEntity<List<LojistaDTO>> buscarTodosLojistas(){
//        return ResponseEntity.ok(lojistaService.buscarTodos());
//    }

    @GetMapping
    public ResponseEntity<List<LojistaDTO>> buscarTodosLojistasPorLoja(@RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        return ResponseEntity.ok(lojistaService.buscarLojistaPorLoja(acesso.getIdLoja()));
    }

    @PostMapping
    public ResponseEntity<LojistaDTO> criarLojista(@Valid @RequestBody LojistaDTO dto, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(lojistaService.salvar(dto, acesso.getIdLoja()));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<LojistaDTO> atualizarLojista(@PathVariable Long id, @Valid @RequestBody LojistaDTO dto,
                                                       @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        dto = lojistaService.atualizar(id, dto, acesso.getIdLoja());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping
    public ResponseEntity<Void> desativarLojistas(@RequestBody List<Long> lojistasIds, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        lojistaService.inativar(lojistasIds, acesso.getIdLoja());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarLojista(@PathVariable Long id, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        lojistaService.remover(id, acesso.getIdLoja());
        return ResponseEntity.noContent().build();
    }
}
