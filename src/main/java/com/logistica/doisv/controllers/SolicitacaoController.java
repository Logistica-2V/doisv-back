package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.CriarSolicitacaoDTO;
import com.logistica.doisv.services.SolicitacaoService;
import com.logistica.doisv.services.validacao.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("doisv/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SolicitacaoService service;

    @PostMapping(value = "/criar")
    public ResponseEntity<?> criarSolicitacao(@Valid @RequestBody CriarSolicitacaoDTO dto, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        service.registrarSolicitacao(dto, acesso.getIdVenda());

        return ResponseEntity.ok("Solicitação de " + dto.tipo() + " realizada com sucesso!");
    }
}
