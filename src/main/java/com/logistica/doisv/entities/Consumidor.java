package com.logistica.doisv.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "tb_Consumidor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consumidor {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idConsumidor;
    
    @Column(nullable=false)
    private String nome;
    private String cpf_cnpj;
    private String email;

    @Column(nullable=false)
    private String celular;
    private String telefone;
    private String endereco;

    @ManyToOne
    @JoinColumn(name= "idLoja")
    private Loja loja;
}
