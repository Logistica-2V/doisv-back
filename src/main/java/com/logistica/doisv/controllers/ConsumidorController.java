package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.ConsumidorDTO;
import com.logistica.doisv.services.ConsumidorService;
import com.logistica.doisv.services.validacao.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("doisv/consumidores")
public class ConsumidorController {
    @Autowired
    private ConsumidorService consumidorService;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<ConsumidorDTO>> buscarTodosConsumidores(@RequestHeader String Authorization) {
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        return ResponseEntity.ok(consumidorService.buscarTodos(acesso.getIdLoja()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ConsumidorDTO> buscarConsumidorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consumidorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ConsumidorDTO> criarConsumidor(@RequestBody ConsumidorDTO dto, @RequestHeader String Authorization) {
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        dto = consumidorService.salvar(dto, acesso.getIdLoja());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.idConsumidor()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ConsumidorDTO> atualizarConsumidor(@PathVariable Long id, @RequestBody ConsumidorDTO dto, @RequestHeader String Authorization) {
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        return ResponseEntity.ok().body(consumidorService.atualizar(dto, id, acesso.getIdLoja()));
    }

    @DeleteMapping(value = "/{id}/permanent")
    public ResponseEntity<Void> deletarConsumidor(@PathVariable Long id, @RequestHeader String Authorization) {
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        consumidorService.remover(id, acesso.getIdLoja());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> desativarConsumidor(@RequestBody List<Long> consumidoresIds, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        consumidorService.inativar(consumidoresIds, acesso.getIdLoja());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(){
        return ResponseEntity.ok("Teste de rota");
    }

}
