package com.logistica.doisv.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.ConsumidorDTO;
import com.logistica.doisv.services.ConsumidorService;
import com.logistica.doisv.services.validacao.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@Controller
@RequestMapping("doisv/consumidores")
@CrossOrigin(origins = "*")
public class ConsumidorController {
    @Autowired
    private ConsumidorService consumidorService;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<ConsumidorDTO>> buscarTodos(@RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        return ResponseEntity.ok(consumidorService.buscarTodos(acesso.getIdLoja()));
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<ConsumidorDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consumidorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ConsumidorDTO> salvar(@RequestBody ConsumidorDTO dto, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        dto.getLoja().setIdLoja(acesso.getIdLoja());
        dto = consumidorService.salvar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getIdConsumidor()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<ConsumidorDTO> atualizar(@PathVariable Long id, @RequestBody ConsumidorDTO dto, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        return ResponseEntity.ok().body(consumidorService.atualizar(dto, id, acesso.getIdLoja()));
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        consumidorService.remover(id);
        return ResponseEntity.noContent().build();
    }

    

}
