package com.logistica.doisv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_Item_Venda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(precision = 6, scale = 2)
    private BigDecimal precoOriginal;
    @Column(precision = 6, scale = 2)
    private BigDecimal precoVendido;
    @Column(precision = 4, scale = 3)
    private BigDecimal percentualVariacao;
    private Integer quantidade;
    private String detalhes;
    private Status status = Status.ATIVO;

    @ManyToOne
    @JoinColumn(name = "idVenda")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "idProduto")
    private Produto produto;

}
