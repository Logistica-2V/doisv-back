package com.logistica.doisv.core.file.dto;

public record ArquivoDTO(byte[] conteudo,
                         String nome,
                         String tipoConteudo) {
}
