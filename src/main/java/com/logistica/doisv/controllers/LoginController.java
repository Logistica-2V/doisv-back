package com.logistica.doisv.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.logistica.doisv.dto.LoginDTO;
import com.logistica.doisv.dto.LoginResponse;
import com.logistica.doisv.services.ConsumidorService;
import com.logistica.doisv.services.LojistaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doisv")
public class LoginController {

    @Autowired
    private LojistaService lojistaService;

    @Autowired
    private ConsumidorService consumidorService;

    @PostMapping("/login")
    public ResponseEntity<?> loginLojista(@Valid @RequestBody LoginDTO dto) {
        String token = lojistaService.login(dto.email(), dto.password());
        if (token != null) {
            return ResponseEntity.ok(new LoginResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos");
    }

    @PostMapping(value = "/consumidores/login")
    public ResponseEntity<?> loginConsumidor(@RequestBody JsonNode consumidorLogin){
        String token = consumidorService.login(consumidorLogin.get("serial").asText(), consumidorLogin.get("senha").asText());
        if(token != null){
            return ResponseEntity.ok(new LoginResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Serial ou senha inválidos");
    }
}
