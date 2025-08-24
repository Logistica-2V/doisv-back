package com.logistica.doisv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_Produto", uniqueConstraints = @UniqueConstraint(columnNames = {"idLoja", "descricao"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduto;
    private String descricao;
    private String unidadeMedida;
    private Double preco;
    private String imagem;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    @ManyToOne
    @JoinColumn(name = "idLoja")
    private Loja loja;
}
