package com.logistica.doisv.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "tb_Consumidor", uniqueConstraints = @UniqueConstraint(columnNames = {"idLoja", "cpf_cnpj"}))
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
    private Status status = Status.ATIVO;

    @ManyToOne
    @JoinColumn(name= "idLoja")
    private Loja loja;

    @OneToMany(mappedBy = "consumidor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Venda> vendas = new ArrayList<>();
}
