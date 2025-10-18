package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Lojista;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LojistaDTO(Long id, @NotBlank String nome, @NotNull String cpf, @NotBlank @Email String email, @NotBlank String password, @NotNull Long idLoja,
                                String status) {

        public LojistaDTO(Lojista lojista){
            this(lojista.getIdLojista(), lojista.getNome(), lojista.getCpf(), lojista.getEmail(), "", lojista.getLoja().getIdLoja(),
                        lojista.getStatus().toString());
        }
}

