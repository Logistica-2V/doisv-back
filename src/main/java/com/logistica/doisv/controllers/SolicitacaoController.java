package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.registro_solicitacao.CriarSolicitacaoDTO;
import com.logistica.doisv.dto.registro_solicitacao.HistoricoSolicitacaoDTO;
import com.logistica.doisv.dto.registro_solicitacao.SolicitacaoDetalhadaDTO;
import com.logistica.doisv.dto.registro_solicitacao.SolicitacaoResumidaDTO;
import com.logistica.doisv.dto.registro_venda.requisicao.ItemDTO;
import com.logistica.doisv.services.SolicitacaoService;
import com.logistica.doisv.services.validacao.TokenService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public ResponseEntity<Page<SolicitacaoResumidaDTO>> buscarTodasSolicitacoes(Pageable pageable, @RequestHeader String Authorization){
        AcessoDTO acessoDTO = tokenService.validarToken(Authorization);
        return ResponseEntity.ok(service.buscarTodos(pageable, acessoDTO.getIdLoja()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SolicitacaoDetalhadaDTO> buscarSolicitacaoPorId(@PathVariable Long id, @RequestHeader String Authorization){
        AcessoDTO acessoDTO = tokenService.validarToken(Authorization);
        return ResponseEntity.ok(service.buscarPorId(id, acessoDTO.getIdLoja()));
    }

    @PostMapping(value = "/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SolicitacaoResumidaDTO> criarSolicitacao(@Valid @RequestPart("solicitacao") CriarSolicitacaoDTO dto,
                                                                   @RequestPart("anexos") List<MultipartFile> anexos,
                                                                   @RequestHeader String Authorization) throws GeneralSecurityException, IOException {
        AcessoDTO acesso = tokenService.validarToken(Authorization);

        return ResponseEntity.ok(service.registrarSolicitacao(dto, anexos ,acesso.getIdVenda()));
    }

    @PostMapping(value = "/atualizar/{id}")
    public ResponseEntity<SolicitacaoDetalhadaDTO> atualizarSolicitacao(@PathVariable Long id,
                                                  @Valid @RequestPart("historico") HistoricoSolicitacaoDTO dto,
                                                  @RequestPart(value = "novosProdutos", required = false) List<ItemDTO> novosProdutos,
                                                  @RequestHeader String Authorization) throws MessagingException {
        AcessoDTO acesso = tokenService.validarToken(Authorization);

        return ResponseEntity.ok(service.atualizarSolicitacao(id, dto, acesso.getIdLoja(), novosProdutos));
    }

    @PutMapping(value = "/aprovar/{id}")
    public ResponseEntity<SolicitacaoResumidaDTO> aprovarSolicitacao(@PathVariable Long id, @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);

        return ResponseEntity.ok(service.aprovarSolicitacao(id, acesso.getIdLoja()));
    }

    @PutMapping(value = "/reprovar/{id}")
    public ResponseEntity<SolicitacaoResumidaDTO> reprovarSolicitacao(@PathVariable Long id, @RequestHeader String Authorization) throws GeneralSecurityException, IOException {
        AcessoDTO acesso = tokenService.validarToken(Authorization);

        return ResponseEntity.ok(service.reprovarSolicitacao(id, acesso.getIdLoja()));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<SolicitacaoResumidaDTO> editarSolicitacao(@PathVariable Long id,
                                                                    @Valid @RequestPart("solicitacao") CriarSolicitacaoDTO dto,
                                                                    @RequestPart("anexos") List<MultipartFile> anexos,
                                                                    @RequestHeader String Authorization) throws GeneralSecurityException, IOException {
        AcessoDTO acessoDTO = tokenService.validarToken(Authorization);

        return ResponseEntity.ok(service.editarSolicitacao(id, dto, anexos, acessoDTO.getIdLoja()));
    }

    @PutMapping(value = "/cancelar/{id}")
    public ResponseEntity<SolicitacaoResumidaDTO> cancelarSolicitacao(@PathVariable Long id,
                                                                      @Valid @RequestBody CriarSolicitacaoDTO dto,
                                                                      @RequestHeader String Authorization) throws GeneralSecurityException, IOException {
        AcessoDTO acessoDTO = tokenService.validarToken(Authorization);

        return ResponseEntity.ok(service.cancelarSolicitacao(id, dto, acessoDTO.getIdLoja()));
    }

}
