package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Loja;
import com.logistica.doisv.entities.Lojista;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LojistaDTO {
        private Long id;
        @NotBlank
        private String nome;
        @NotNull
        private String cpf;
        @NotBlank 
        @Email
        private String email;
        @NotBlank
        private String password;
        @NotNull
        private Loja loja;

        public LojistaDTO(Lojista lojista){
            id = lojista.getId();
            nome = lojista.getNome();
            cpf = lojista.getCpf();
            email = lojista.getEmail();
            password = lojista.getPassword();
            loja = lojista.getLoja();
        }
}

