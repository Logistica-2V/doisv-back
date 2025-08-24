package com.logistica.doisv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tb_Lojista")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lojista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    @Column(unique = true)
    private String email;
    @Column(name = "senha")
    private String password;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "idLoja")
    private Loja loja;

}
