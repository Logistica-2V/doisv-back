package com.logistica.doisv.dto;

import com.logistica.doisv.entities.Consumidor;
import com.logistica.doisv.entities.Loja;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumidorDTO {
    private Long idConsumidor;
    @NotBlank
    private String nome;
    private String cpf_cnpj;
    private String email;
    @NotBlank
    private String celular;
    private String telefone;
    private String endereco;
    @NotNull
    private Loja loja;

    public ConsumidorDTO(Consumidor consumidor){
        idConsumidor = consumidor.getIdConsumidor();
        nome = consumidor.getNome();
        cpf_cnpj = consumidor.getCpf_cnpj();
        email = consumidor.getEmail();
        celular = consumidor.getCelular();
        telefone = consumidor.getTelefone();
        endereco = consumidor.getEndereco();
        loja = consumidor.getLoja();
    }
}
