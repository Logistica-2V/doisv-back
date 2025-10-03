package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Consumidor;
import jakarta.validation.constraints.NotBlank;

public record ConsumidorDTO(Long idConsumidor, @NotBlank String nome, String cpf_cnpj, String email, @NotBlank String celular, String telefone, 
                                String endereco, String status) {
    
    public ConsumidorDTO(Consumidor consumidor){
        this(consumidor.getIdConsumidor(), consumidor.getNome(), consumidor.getCpf_cnpj(), consumidor.getEmail(), consumidor.getCelular(), consumidor.getTelefone()
                , consumidor.getEndereco(), consumidor.getStatus().toString());
    }
}
