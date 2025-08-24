package com.logistica.doisv.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_Loja")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLoja;
    private String nome;
    private String cnpj;
    private String segmento;
    private String logo;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "loja")
    @JsonIgnore
    private Set<Lojista> lojistas = new HashSet<>();

    @OneToMany(mappedBy = "loja")
    @JsonIgnore
    private List<Produto> produtos = new ArrayList<>();

}
