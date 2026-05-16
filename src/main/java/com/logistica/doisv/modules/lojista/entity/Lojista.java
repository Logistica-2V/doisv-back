package com.logistica.doisv.modules.lojista.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logistica.doisv.modules.autenticacao.entity.RecuperarSenha;
import com.logistica.doisv.core.enums.Status;
import com.logistica.doisv.modules.loja.entity.Loja;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_Lojista", uniqueConstraints = @UniqueConstraint(columnNames = {"idLoja", "cpf"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lojista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLojista;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 14)
    private String cpf;

    @Column(unique = true, nullable = false, length = 160)
    private String email;

    @Column(name = "senha", nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.ATIVO;

    @Column(nullable = false)
    private Boolean admin = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idLoja", nullable = false)
    private Loja loja;

    @OneToMany(mappedBy = "lojista", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<RecuperarSenha> recuperacoesSenha = new ArrayList<>();
}
