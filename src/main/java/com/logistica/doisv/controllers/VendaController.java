package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.registro_venda.requisicao.RegistroVendaDTO;
import com.logistica.doisv.dto.registro_venda.resposta.VendaDTO;
import com.logistica.doisv.services.VendaService;
import com.logistica.doisv.services.validacao.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("doisv/vendas")
public class VendaController {

    @Autowired
    private VendaService service;

    @Autowired
    private TokenService tokenService;


    @PostMapping
    public ResponseEntity<VendaDTO> criarVenda(@RequestBody RegistroVendaDTO dto, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        VendaDTO venda = service.salvar(dto, acesso.getIdLoja());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(venda.idVenda()).toUri();
        return ResponseEntity.created(uri).body(venda);
    }
}
