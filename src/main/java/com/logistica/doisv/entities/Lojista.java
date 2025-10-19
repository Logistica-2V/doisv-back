package com.logistica.doisv.entities;

import com.logistica.doisv.entities.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_Lojista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lojista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLojista;
    private String nome;
    private String cpf;
    @Column(unique = true)
    private String email;
    @Column(name = "senha")
    private String password;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    @ManyToOne
    @JoinColumn(name = "idLoja")
    private Loja loja;

}
