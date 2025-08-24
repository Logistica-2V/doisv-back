package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.LojaDTO;
import com.logistica.doisv.services.LojaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.List;


@Controller
@RequestMapping("doisv/lojas")
@CrossOrigin(origins = "*")
public class LojaController {
    @Autowired
    LojaService lojaService;

    @GetMapping
    public ResponseEntity<List<LojaDTO>> buscarLojas() {
        return ResponseEntity.ok().body(lojaService.buscarTodos());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LojaDTO> buscarLojaPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(lojaService.buscarPorId(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LojaDTO> salvar(@Valid @RequestPart("loja") LojaDTO dto, @RequestPart("logo") MultipartFile logo) throws GeneralSecurityException, IOException {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.idLoja()).toUri();
        return ResponseEntity.created(uri).body(lojaService.salvar(dto, logo));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestPart("loja") LojaDTO dto, @RequestPart("logo") MultipartFile logo) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok().body(lojaService.atualizar(id, dto, logo));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        lojaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
