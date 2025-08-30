package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.LojistaDTO;
import com.logistica.doisv.services.LojistaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("doisv/lojistas")
@CrossOrigin(origins = "*")
public class LojistaController {
    @Autowired
    private LojistaService lojistaService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<LojistaDTO> buscarLojistaPorId(@PathVariable Long id){
        return ResponseEntity.ok(lojistaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<LojistaDTO>> buscarTodosLojistas(){
        return ResponseEntity.ok(lojistaService.buscarTodos());
    }

    @GetMapping(params = "lojaId")
    public ResponseEntity<List<LojistaDTO>> buscarLojistaPorLoja(@RequestParam Long lojaId){
        return ResponseEntity.ok(lojistaService.buscarLojistaPorLoja(lojaId));
    }

    @PostMapping
    public ResponseEntity<LojistaDTO> criarLojista(@Valid @RequestBody LojistaDTO dto){
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(lojistaService.salvar(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<LojistaDTO> atualizarLojista(@PathVariable Long id, @Valid @RequestBody LojistaDTO dto){
        dto = lojistaService.atualizar(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}/permanent")
    public ResponseEntity<Void> deletarLojista(@PathVariable Long id){
        lojistaService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> desativarLojistas(@RequestBody List<Long> lojistasIds){
        lojistaService.inativar(lojistasIds);
        return ResponseEntity.noContent().build();
    }
}
