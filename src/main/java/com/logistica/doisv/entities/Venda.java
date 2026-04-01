package com.logistica.doisv.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.entities.enums.StatusPedido;
import com.logistica.doisv.services.exceptions.EdicaoNaoPermitidaException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_Venda")
@Getter
@Setter
@Builder
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

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemVenda> itensVenda = new ArrayList<>();

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Solicitacao> solicitacoes = new ArrayList<>();

    public Venda(Loja loja, Consumidor consumidor, String statusPedido, BigDecimal desconto, String formaPagamento, Integer prazoTroca, Integer prazoDevolucao){
        this.loja = loja;
        this.consumidor = consumidor;
        this.statusPedido = StatusPedido.converterDeStringParaEnum(statusPedido);
        this.desconto = desconto;
        this.formaPagamento = formaPagamento;
        this.prazoTroca = prazoTroca;
        this.prazoDevolucao = prazoDevolucao;
        this.precoTotal = BigDecimal.valueOf(0);
        this.dataCriacao = Instant.now();
    }

    public static Venda criar(Consumidor consumidor, Loja loja, StatusPedido statusPedido, String formaPagamento,
                              BigDecimal desconto, Integer prazoTroca, Integer prazoDevolucao){

        return Venda.builder()
                .consumidor(consumidor)
                .loja(loja)
                .statusPedido(statusPedido)
                .formaPagamento(formaPagamento)
                .desconto(desconto)
                .prazoTroca(prazoTroca)
                .prazoDevolucao(prazoDevolucao)
                .dataCriacao(Instant.now())
                .itensVenda(new ArrayList<>())
                .status(Status.ATIVO)
                .build();
    }

    public void atualizarCampos(StatusPedido statusPedido, String formaPagamento,
                                BigDecimal desconto, Integer prazoTroca, Integer prazoDevolucao) {
        validarSeAlteravel();

        this.statusPedido = statusPedido;
        this.formaPagamento = formaPagamento;
        this.desconto = desconto;
        this.prazoTroca = prazoTroca;
        this.prazoDevolucao = prazoDevolucao;
    }

    public void adicionarItem(ItemVenda itemVenda){
        this.itensVenda.add(itemVenda);
    }

    public void calcularPrecoTotal(){
        BigDecimal precoTotalSemDesconto = this.itensVenda.stream()
                .map(item ->
                        item.getPrecoVendido()
                                .multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal descontoAplicado = this.desconto != null ? this.desconto : BigDecimal.ZERO;

        this.precoTotal = precoTotalSemDesconto.subtract(descontoAplicado).setScale(2, RoundingMode.HALF_UP);
    }

    private void validarSeAlteravel(){
        if(this.statusPedido.equals(StatusPedido.ENTREGUE) || this.statusPedido.equals(StatusPedido.CANCELADA)){
            throw new EdicaoNaoPermitidaException("Status atual da venda não permite edição de dados.");
        }
    }

    public boolean pertenceAoConsumidor(Long idConsumidor) {
        return this.consumidor.getIdConsumidor().equals(idConsumidor);
    }

    public void atualizarConsumidor(Consumidor novoConsumidor) {
        this.consumidor = novoConsumidor;
    }

    public void removerItens(List<Long> idsParaManter) {
        this.itensVenda.removeIf(item -> !idsParaManter.contains(item.getId()));
    }

    public void atualizarItem(Long idItemVenda, BigDecimal precoVendido, Double quantidade, String detalhes) {
        this.itensVenda.stream()
                .filter(item -> item.getId().equals(idItemVenda))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado na venda."))
                .atualizarInformacoes(precoVendido, quantidade, detalhes);
    }
}
