package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Lojista;
import com.logistica.doisv.util.validacao.CpfCnpj;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LojistaDTO(Long id, @NotBlank String nome, @CpfCnpj @NotNull String cpf, @NotBlank @Email String email, @NotBlank String password,
                         @NotNull Long idLoja, String status) {

    public LojistaDTO {
        cpf = cpf != null ? cpf.replaceAll("[^0-9A-Za-z]", "").toUpperCase() : null;
    }

        public LojistaDTO(Lojista lojista){
            this(lojista.getIdLojista(), lojista.getNome(), lojista.getCpf(), lojista.getEmail(), "", lojista.getLoja().getIdLoja(),
                        lojista.getStatus().toString());
        }
}

