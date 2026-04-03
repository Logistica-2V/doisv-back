package com.logistica.doisv.dto.registro_venda.resposta;

import com.logistica.doisv.entities.Loja;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LojaVenda", description = "Dados resumidos da loja em uma venda")
public record LojaVendaDTO(
        @Schema(description = "Identificador único da loja", example = "1")
        Long id,

        @Schema(description = "Nome da loja", example = "Loja Central")
        String nome,

        @Schema(description = "E-mail da loja", example = "contato@lojacentral.com")
        String email) {

    public LojaVendaDTO(Loja loja){
        this(loja.getIdLoja(), loja.getNome(), loja.getEmail());
    }
}
