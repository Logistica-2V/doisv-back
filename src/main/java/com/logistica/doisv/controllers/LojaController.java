package com.logistica.doisv.controllers;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.logistica.doisv.dto.LojaDTO;
import com.logistica.doisv.services.LojaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Controller
@RequestMapping("doisv/lojas")
@CrossOrigin(origins = "*")
@Tag(name = "Lojas", description = "Manter Lojas")
public class LojaController {
    @Autowired
    LojaService lojaService;

    @GetMapping
    @Operation(summary = "Buscar Todas Lojas", description = "Rota responsável por buscar todas as Lojas")    
    public ResponseEntity<List<LojaDTO>> buscarLojas(){
        return ResponseEntity.ok().body(lojaService.buscarTodos());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Buscar por ID", description = "Rota responsável por buscar Lojas pelo seu ID")
    public ResponseEntity<LojaDTO> buscarLojaPorId(@PathVariable Long id){
        return ResponseEntity.ok().body(lojaService.buscarPorId(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cadastrar Loja", description = "Rota responsável por cadastrar Loja")
    public ResponseEntity<LojaDTO> salvar(@Valid @RequestPart("loja") LojaDTO dto, @RequestPart("logo") MultipartFile logo) throws GeneralSecurityException, IOException {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getIdLoja()).toUri();
        return ResponseEntity.created(uri).body(lojaService.salvar(dto, logo));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualizar Loja", description = "Rota responsável por ataulizar os dados da loja através do seu ID")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestPart("loja") LojaDTO dto, @RequestPart("logo") MultipartFile logo) throws GeneralSecurityException, IOException{
        return ResponseEntity.ok().body(lojaService.atualizar(id, dto, logo));
    }

}
