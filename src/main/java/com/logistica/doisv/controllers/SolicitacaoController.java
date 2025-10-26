package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.CriarSolicitacaoDTO;
import com.logistica.doisv.services.SolicitacaoService;
import com.logistica.doisv.services.validacao.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("doisv/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SolicitacaoService service;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> criarSolicitacao(@Valid @RequestPart("solicitacao") CriarSolicitacaoDTO dto, @RequestPart("anexos") List<MultipartFile> anexos,
                                              @RequestHeader String Authorization) throws GeneralSecurityException, IOException {
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        service.registrarSolicitacao(dto, anexos ,acesso.getIdVenda());

        return ResponseEntity.ok("Solicitação de " + dto.tipo() + " realizada com sucesso!");
    }
}
