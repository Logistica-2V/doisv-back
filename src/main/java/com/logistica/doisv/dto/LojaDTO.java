package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Loja;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LojaDTO {

    private Long idLoja;
    @NotBlank
    private String nome;
    private String cnpj;
    @NotBlank
    private String segmento;
    private String logo;
    @Email
    private String email;

    public LojaDTO(Loja loja) {
        idLoja = loja.getIdLoja();
        nome = loja.getNome();
        cnpj = loja.getCnpj();
        segmento = loja.getSegmento();
        logo = loja.getLogo();
        email = loja.getEmail();
    }
}
