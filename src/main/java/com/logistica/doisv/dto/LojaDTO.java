package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Loja;
import com.logistica.doisv.util.validacao.CpfCnpj;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LojaDTO (Long idLoja, @NotBlank String nome, @CpfCnpj @NotNull String cnpj,
                       @NotBlank String segmento, String logo, @Email @NotBlank String email, String status){

    public LojaDTO {
        cnpj = cnpj.replaceAll("[^0-9A-Za-z]", "").toUpperCase();
    }

    public LojaDTO(Loja loja) {
        this(loja.getIdLoja(), loja.getNome(), loja.getCnpj(), loja.getSegmento(), loja.getLogo(), loja.getEmail(), loja.getStatus().toString());
    }
}
