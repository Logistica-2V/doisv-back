package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.LojistaDTO;
import com.logistica.doisv.services.LojistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("doisv/lojistas")
@CrossOrigin(origins = "*")
@Tag(name = "Lojista", description = "Manter lojista")
public class LojistaController {
    @Autowired
    private LojistaService lojistaService;

    @GetMapping(value = "/{id}")
    @Operation(summary = "Buscar por ID", description = "Rota responsável por buscar Lojista pelo seu ID")
    public ResponseEntity<LojistaDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(lojistaService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Buscar todos", description = "Rota responsável por retornar todos lojistas.")
    public ResponseEntity<List<LojistaDTO>> listarLojistas(){
        return ResponseEntity.ok(lojistaService.buscarTodos());
    }

    @GetMapping(params = "lojaId")
    @Operation(summary = "Buscar todos por loja", description = "Rota responsável por retornar todos lojistas de uma loja.")
    public ResponseEntity<List<LojistaDTO>> listarLojistaPorLoja(@RequestParam Long lojaId){
        return ResponseEntity.ok(lojistaService.buscarLojistaPorLoja(lojaId));
    }

    @PostMapping
    @Operation(summary = "Cadastrar lojista", description = "Rota responsável por cadastrar um lojista.")
    public ResponseEntity<LojistaDTO> salvar(@Valid @RequestBody LojistaDTO dto){
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(lojistaService.salvar(dto));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualizar lojista", description = "Rota responsável por atualizar um lojista através do seu ID.")
    public ResponseEntity<LojistaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LojistaDTO dto){
        dto = lojistaService.atualizar(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Excluir lojista", description = "Rota responsável por excluir um lojista através do seu ID.")
    public ResponseEntity<Void> remover(@PathVariable Long id){
        lojistaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
