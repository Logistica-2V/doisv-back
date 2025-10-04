package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.dto.registro_venda.requisicao.RegistroVendaDTO;
import com.logistica.doisv.dto.registro_venda.resposta.VendaDTO;
import com.logistica.doisv.services.VendaService;
import com.logistica.doisv.services.validacao.TokenService;
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
        Page<VendaDTO> dto = service.buscarTodasVendasPorLoja(pageable, validarLoja(Authorization));
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VendaDTO> buscarVendaPorId(@PathVariable Long id){
        VendaDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<VendaDTO> criarVenda(@RequestBody RegistroVendaDTO dto, @RequestHeader String Authorization){
        VendaDTO venda = service.salvar(dto, validarLoja(Authorization));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(venda.idVenda()).toUri();
        return ResponseEntity.created(uri).body(venda);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<VendaDTO> atualizarVenda(@PathVariable Long id, @RequestBody RegistroVendaDTO dto, @RequestHeader String Authorization){
        VendaDTO venda = service.atualizar(id, dto, validarLoja(Authorization));
        return ResponseEntity.ok(venda);
    }

    @DeleteMapping
    public ResponseEntity<Void> desativarVenda(@RequestBody List<Long> vendasIds, @RequestHeader String Authorization){
        service.inativar(vendasIds, validarLoja(Authorization));
        return ResponseEntity.noContent().build();
    }

    private Long validarLoja(String token){
        AcessoDTO acesso = tokenService.validarToken(token);
        return acesso.getIdLoja();
    }
}
