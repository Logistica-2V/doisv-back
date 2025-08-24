package com.logistica.doisv.entities;

import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name= "idLoja")
    private Loja loja;
}
