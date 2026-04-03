package com.logistica.doisv.controllers;

import com.logistica.doisv.controllers.docs.FeedbackApi;
import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.registro_feedback.FeedbackDTO;
import com.logistica.doisv.dto.registro_feedback.FeedbackResumidoDTO;
import com.logistica.doisv.services.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("doisv/feedbacks")
public class FeedbackController implements FeedbackApi {
    @Autowired
    private FeedbackService service;

    @GetMapping("/{idFeedback}")
    public ResponseEntity<FeedbackDTO> buscarFeedbackPorId(@PathVariable Long idFeedback,
                                                           @AuthenticationPrincipal AcessoDTO usuarioLogado){

        return ResponseEntity.ok(service.buscarPorId(idFeedback, usuarioLogado.getIdLoja()));
    }

    @GetMapping("/solicitacoes/{idSolicitacao}")
    public ResponseEntity<List<FeedbackDTO>> buscarFeedbacksPorSolicitacao(@PathVariable Long idSolicitacao,
                                                                           @AuthenticationPrincipal AcessoDTO usuarioLogado){

        return ResponseEntity.ok(service.buscarPorIdSolicitacao(idSolicitacao, usuarioLogado));
    }

    @GetMapping("/lojas/{idLoja}")
    public ResponseEntity<Page<FeedbackResumidoDTO>> buscarFeedbacksPorLoja(@PathVariable UUID idLoja,
                                                                            @RequestParam(defaultValue = "180")
                                                                            Integer periodo,
                                                                            Pageable pageable){
        return ResponseEntity.ok(service.buscarTodosPorLoja(pageable, idLoja, periodo));
    }

    @PostMapping
    public ResponseEntity<FeedbackDTO> criarFeedback(@Valid @RequestBody FeedbackDTO dto,
                                                     @AuthenticationPrincipal AcessoDTO usuarioLogado){
        dto = service.salvar(dto,
                usuarioLogado.getIdConsumidor(),
                usuarioLogado.getIdLoja(),
                usuarioLogado.getIdVenda());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.idFeedback()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

}
