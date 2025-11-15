package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.CriarSolicitacaoDTO;
import com.logistica.doisv.dto.HistoricoSolicitacaoDTO;
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

    @PostMapping(value = "/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> criarSolicitacao(@Valid @RequestPart("solicitacao") CriarSolicitacaoDTO dto, @RequestPart("anexos") List<MultipartFile> anexos,
                                              @RequestHeader String Authorization) throws GeneralSecurityException, IOException {
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        service.registrarSolicitacao(dto, anexos ,acesso.getIdVenda());

        return ResponseEntity.ok("Solicitação de " + dto.tipo() + " realizada com sucesso!");
    }

    @PostMapping(value = "/atualizar/{id}")
    public ResponseEntity<?> atualizarSolicitacao(@PathVariable Long id,
                                                  @Valid @RequestPart("historico") HistoricoSolicitacaoDTO dto,
                                                  @RequestPart(value = "idsNovosProdutos", required = false) List<Long> idsNovosProdutos,
                                                  @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        service.atualizarSolicitacao(id, dto, acesso.getIdLoja(), idsNovosProdutos);

        return ResponseEntity.ok("Solicitação atualizada!");
    }

    @PutMapping(value = "/aprovar/{id}")
    public ResponseEntity<?> aprovarSolicitacao(@PathVariable Long id, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        service.aprovarSolicitacao(id, acesso.getIdLoja());

        return ResponseEntity.ok("Solicitação ID " + id + " aprovada com sucesso!");
    }

    @PutMapping(value = "/reprovar/{id}")
    public ResponseEntity<?> reprovarSolicitacao(@PathVariable Long id, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        service.reprovarSolicitacao(id, acesso.getIdLoja());

        return ResponseEntity.ok("Solicitação ID " + id + " reprovada!");
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> editarSolicitacao(@PathVariable Long id,@RequestPart("solicitacao") CriarSolicitacaoDTO dto,
                                               @RequestPart("anexos") List<MultipartFile> anexos, @RequestHeader String Authorization) throws GeneralSecurityException, IOException {
        AcessoDTO acessoDTO = tokenService.validarToken(Authorization);
        service.editarSolicitacao(id, dto, anexos, acessoDTO.getIdLoja());

        return ResponseEntity.ok("Solicitação ID " + id + " editada!");
    }

}
