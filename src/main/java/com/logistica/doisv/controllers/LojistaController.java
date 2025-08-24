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
public class LojistaController {
    @Autowired
    private LojistaService lojistaService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<LojistaDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(lojistaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<LojistaDTO>> listarLojistas(){
        return ResponseEntity.ok(lojistaService.buscarTodos());
    }

    @GetMapping(params = "lojaId")
    public ResponseEntity<List<LojistaDTO>> listarLojistaPorLoja(@RequestParam Long lojaId){
        return ResponseEntity.ok(lojistaService.buscarLojistaPorLoja(lojaId));
    }

    @PostMapping
    public ResponseEntity<LojistaDTO> salvar(@Valid @RequestBody LojistaDTO dto){
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(lojistaService.salvar(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<LojistaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LojistaDTO dto){
        dto = lojistaService.atualizar(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id){
        lojistaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
