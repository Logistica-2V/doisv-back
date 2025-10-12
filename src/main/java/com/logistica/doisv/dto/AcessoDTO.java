package com.logistica.doisv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcessoDTO {

    private TipoUsuario tipo;
    private String subject;
    private String nome;
    private Long idLoja;

    private Long idLojista;
    private Long idConsumidor;
    private String serialVenda;
    private Long idVenda;

    public enum TipoUsuario{
        LOJISTA,
        CONSUMIDOR
    }
}
