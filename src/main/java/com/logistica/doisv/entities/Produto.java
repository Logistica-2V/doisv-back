package com.logistica.doisv.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
    private String statusProduto;

    @ManyToOne
    @JoinColumn(name = "idLoja")
    private Loja loja;
}
