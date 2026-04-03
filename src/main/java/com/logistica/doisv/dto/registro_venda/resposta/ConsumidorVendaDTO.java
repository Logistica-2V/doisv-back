package com.logistica.doisv.dto.registro_venda.resposta;

import com.logistica.doisv.entities.Consumidor;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ConsumidorVenda", description = "Dados resumidos do consumidor em uma venda")
public record ConsumidorVendaDTO(
        @Schema(description = "Identificador único do consumidor", example = "1")
        Long id,

        @Schema(description = "Nome completo do consumidor", example = "Maria da Silva")
        String nome,

        @Schema(description = "E-mail do consumidor", example = "maria@email.com")
        String email,

        @Schema(description = "Número de celular do consumidor", example = "11999998888")
        String celular,

        @Schema(description = "Endereço completo do consumidor", example = "Rua das Flores, 123 - São Paulo/SP")
        String endereco) {

    public ConsumidorVendaDTO(Consumidor consumidor){
        this(consumidor.getIdConsumidor(), consumidor.getNome(), consumidor.getEmail(),
                consumidor.getCelular(), consumidor.getEndereco());
    }
}
