package com.logistica.doisv.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.entities.enums.StatusSolicitacao;
import com.logistica.doisv.entities.enums.TipoSolicitacao;
import com.logistica.doisv.services.exceptions.RegraNegocioException;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tb_Solicitacao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoSolicitacao tipoSolicitacao;
    private Double quantidade;
    private String motivo;
    private Instant dataSolicitacao;
    private LocalDateTime dataAtualizacao;
    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusSolicitacao = StatusSolicitacao.PENDENTE;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idVenda")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "idConsumidor")
    private Consumidor consumidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idItemVenda", nullable = false)
    private ItemVenda itemVenda;

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<AnexoSolicitacao> anexos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<HistoricoSolicitacao> historicos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Feedback> feedbacks = new HashSet<>();

    public static Solicitacao criar(Double quantidade, String motivo, Venda venda,
            ItemVenda itemVenda, TipoSolicitacao tipoSolicitacao) {
        return Solicitacao.builder()
                .quantidade(quantidade)
                .motivo(motivo)
                .tipoSolicitacao(tipoSolicitacao)
                .statusSolicitacao(StatusSolicitacao.PENDENTE)
                .status(Status.ATIVO)
                .venda(venda)
                .consumidor(venda.getConsumidor())
                .itemVenda(itemVenda)
                .dataSolicitacao(Instant.now())
                .anexos(new LinkedHashSet<>())
                .historicos(new LinkedHashSet<>())
                .feedbacks(new HashSet<>())
                .build();
    }

    public void editar(TipoSolicitacao tipoSolicitacao, Double quantidade, String motivo) {
        this.tipoSolicitacao = tipoSolicitacao;
        this.quantidade = quantidade;
        this.motivo = motivo;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void aprovar(ItemVenda itemOriginal) {
        validarSeAlteravel();

        this.historicos.add(HistoricoSolicitacao.aprovacao(this));
        this.statusSolicitacao = StatusSolicitacao.APROVADA;
        this.dataAtualizacao = LocalDateTime.now();

        ItemVenda novoItem = construirItemSolicitacao(itemOriginal);
        itemOriginal.reduzirQuantidade(this.quantidade);
        this.venda.getItensVenda().add(novoItem);
        this.itemVenda = novoItem;
    }

    public void reprovar() {
        validarSeAlteravel();

        this.statusSolicitacao = StatusSolicitacao.REJEITADA;
        this.status = Status.INATIVO;
        this.dataAtualizacao = LocalDateTime.now();
        this.historicos.add(HistoricoSolicitacao.rejeicao(this));
    }

    public void cancelar() {
        validarSeCancelavel();

        this.itemVenda.restaurar();
        this.statusSolicitacao = StatusSolicitacao.CANCELADA;
        this.status = Status.INATIVO;
        this.dataAtualizacao = LocalDateTime.now();
        this.historicos.add(HistoricoSolicitacao.cancelamento(this));
    }

    public void atualizar(StatusSolicitacao novoStatus, String observacao) {
        validarSeAtualizavel();
        validarAvanco(novoStatus);

        this.historicos.add(HistoricoSolicitacao.criar(novoStatus, this, observacao));
        this.statusSolicitacao = novoStatus;
        this.dataAtualizacao = LocalDateTime.now();
    }

    private void validarSeAlteravel() {
        if (this.status == Status.INATIVO || this.statusSolicitacao != StatusSolicitacao.PENDENTE) {
            throw new RegraNegocioException(String.format(
                    "A solicitação de %s ID %s não pode mais ser alterada.",
                    this.tipoSolicitacao.getDescricao().toLowerCase(), this.id));
        }
    }

    private void validarSeCancelavel() {
        boolean statusInvalido = this.statusSolicitacao == StatusSolicitacao.EM_TRANSITO ||
                this.statusSolicitacao == StatusSolicitacao.CONCLUIDA ||
                this.statusSolicitacao == StatusSolicitacao.CANCELADA ||
                this.status == Status.INATIVO;

        if (statusInvalido) {
            throw new RegraNegocioException(String.format("""
                    A solicitação de %s ID %s não pode mais ser cancelada no status atual:
                    Status Solicitação: %s
                    Status: %s.""",
                    this.tipoSolicitacao.getDescricao().toLowerCase(),
                    this.id,
                    this.statusSolicitacao.getStatusSolicitacao(),
                    this.status.name()));
        }
    }

    private void validarSeAtualizavel() {
        boolean statusInvalido = this.statusSolicitacao == StatusSolicitacao.PENDENTE ||
                this.statusSolicitacao == StatusSolicitacao.REJEITADA ||
                this.statusSolicitacao == StatusSolicitacao.CONCLUIDA ||
                this.status == Status.INATIVO;

        if (statusInvalido) {
            throw new RegraNegocioException(String.format("""
                    A solicitação de %s ID %s não pode mais ser alterada no status atual:
                    Status Solicitação: %s
                    Status: %s.""",
                    this.tipoSolicitacao.getDescricao(),
                    this.id,
                    this.statusSolicitacao.getStatusSolicitacao(),
                    this.status.getStatusItem()));
        }
    }

    private ItemVenda construirItemSolicitacao(ItemVenda origem) {
        return ItemVenda.builder()
                .precoOriginal(origem.getPrecoOriginal())
                .precoVendido(origem.getPrecoVendido())
                .percentualVariacao(origem.getPercentualVariacao())
                .quantidade(this.quantidade)
                .detalhes(String.format("Item para %s - Solicitação número: %d",
                        this.tipoSolicitacao.getDescricao(), this.id))
                .status(Status.INATIVO)
                .venda(this.venda)
                .produto(origem.getProduto())
                .build();
    }

    private void validarAvanco(StatusSolicitacao novoStatus) {
        if (!this.statusSolicitacao.podeAvancarPara(novoStatus)) {
            throw new RegraNegocioException(String.format(
                    "Não é possível alterar o status de %s para %s.",
                    this.statusSolicitacao.getStatusSolicitacao(),
                    novoStatus.getStatusSolicitacao()));
        }
    }
}
