package com.logistica.doisv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcessoDTO {
    private Long idLojista;
    private String emailLojista;
    private String nomeLojista;
    private Long idLoja;
}
