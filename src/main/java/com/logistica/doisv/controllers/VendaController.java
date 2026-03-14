package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.registro_venda.requisicao.RegistroVendaDTO;
import com.logistica.doisv.dto.registro_venda.resposta.VendaDTO;
import com.logistica.doisv.services.VendaService;
import com.logistica.doisv.services.validacao.TokenService;
import jakarta.mail.MessagingException;
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
@RequestMapping("doisv/vendas")
public class VendaController {

    @Autowired
    private VendaService service;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public ResponseEntity<Page<VendaDTO>> buscarTodasVendas(Pageable pageable, @RequestHeader String Authorization){
        Page<VendaDTO> dto = service.buscarTodasVendasPorLoja(pageable, extrairIdLoja(Authorization));
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VendaDTO> buscarVendaPorId(@PathVariable Long id, @RequestHeader String Authorization){
        VendaDTO dto = service.buscarPorId(id, extrairIdLoja(Authorization));
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/me")
    public ResponseEntity<VendaDTO> buscarVendaPorToken(@RequestHeader String Authorization){
        AcessoDTO acesso = tokenService.validarToken(Authorization);
        return ResponseEntity.ok(service.buscarPorId(acesso.getIdVenda(), extrairIdLoja(Authorization)));
    }

    @PostMapping
    public ResponseEntity<VendaDTO> criarVenda(@Valid @RequestBody RegistroVendaDTO dto, @RequestHeader String Authorization) throws MessagingException {
        VendaDTO venda = service.salvar(dto, extrairIdLoja(Authorization));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(venda.idVenda()).toUri();
        return ResponseEntity.created(uri).body(venda);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<VendaDTO> atualizarVenda(@PathVariable Long id, @Valid @RequestBody RegistroVendaDTO dto, @RequestHeader String Authorization) throws MessagingException {
        VendaDTO venda = service.atualizar(id, dto, extrairIdLoja(Authorization));
        return ResponseEntity.ok(venda);
    }

    @PatchMapping
    public ResponseEntity<Void> desativarVenda(@RequestBody List<Long> vendasIds, @RequestHeader String Authorization){
        service.inativar(vendasIds, extrairIdLoja(Authorization));
        return ResponseEntity.noContent().build();
    }

    private Long extrairIdLoja(String token){
        AcessoDTO acesso = tokenService.validarToken(token);
        return acesso.getIdLoja();
    }
}
