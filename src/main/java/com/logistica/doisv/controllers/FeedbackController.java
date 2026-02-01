package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.registro_feedback.FeedbackDTO;
import com.logistica.doisv.dto.registro_feedback.FeedbackResumidoDTO;
import com.logistica.doisv.services.FeedbackService;
import com.logistica.doisv.services.validacao.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("doisv/feedbacks")
public class FeedbackController {
    @Autowired
    private FeedbackService service;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> buscarFeedbackPorId(@PathVariable Long id,
                                                           @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        return ResponseEntity.ok(service.buscarPorId(id, acesso.getIdLoja()));
    }

    @GetMapping("/solicitacoes/{idSolicitacao}")
    public ResponseEntity<List<FeedbackDTO>> buscarFeedbacksPorSolicitacao(@PathVariable Long idSolicitacao,
                                                                           @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        return ResponseEntity.ok(service.buscarPorIdSolicitacao(idSolicitacao, acesso));
    }

    @GetMapping("/lojas/{idLoja}")
    public ResponseEntity<Page<FeedbackResumidoDTO>> buscarFeedbacksPorLoja(@PathVariable Long idLoja,
                                                                            @RequestParam(defaultValue = "180") Integer periodo,
                                                                            Pageable pageable){
        return ResponseEntity.ok(service.buscarTodosPorLoja(pageable, idLoja, periodo));
    }

    @PostMapping
    public ResponseEntity<FeedbackDTO> criarFeedback(@Valid @RequestBody FeedbackDTO dto,
                                                     @RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        dto = service.salvar(dto, acesso.getIdConsumidor());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.idFeedback()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

}
