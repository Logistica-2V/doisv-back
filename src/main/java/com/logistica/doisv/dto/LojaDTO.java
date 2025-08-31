package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Loja;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LojaDTO (Long idLoja, @NotBlank String nome, String cnpj, @NotBlank String segmento, String logo, @Email String email, String status){

    public LojaDTO(Loja loja) {
        this(loja.getIdLoja(), loja.getNome(), loja.getCnpj(), loja.getSegmento(), loja.getLogo(), loja.getEmail(), loja.getStatus().toString());
    }
}
