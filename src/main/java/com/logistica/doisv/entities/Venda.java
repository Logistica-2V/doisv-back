package com.logistica.doisv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_Venda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String serialVenda;
    private String senha;
    @Column(precision = 6, scale = 2)
    private BigDecimal precoTotal;
    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;
    private BigDecimal desconto;
    private String formaPagamento;
    private Integer prazoTroca;
    private Integer prazoDevolucao;
    private Instant dataCriacao;
    private LocalDate dataEntrega;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    @ManyToOne
    @JoinColumn(name = "idLoja")
    private Loja loja;

    @ManyToOne
    @JoinColumn(name = "idConsumidor")
    private Consumidor consumidor;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemVenda> itensVenda = new ArrayList<>();

    public Venda(Loja loja, Consumidor consumidor, String statusPedido, BigDecimal desconto, String formaPagamento, Integer prazoTroca, Integer prazoDevolucao){
        this.loja = loja;
        this.consumidor = consumidor;
        this.statusPedido = StatusPedido.converterParaString(statusPedido);
        this.desconto = desconto;
        this.formaPagamento = formaPagamento;
        this.prazoTroca = prazoTroca;
        this.prazoDevolucao = prazoDevolucao;
        this.precoTotal = BigDecimal.valueOf(0);
        this.dataCriacao = Instant.now();
    }
}
