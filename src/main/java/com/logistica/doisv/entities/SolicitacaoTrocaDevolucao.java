package com.logistica.doisv.entities;

import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.entities.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_Solicitacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoTrocaDevolucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoSolicitacao tipoSolicitacao;
    private BigDecimal quantidade;
    private String motivo;
    private Instant dataSolicitacao;
    private LocalDateTime dataAtualizacao;
    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusSolicitacao = StatusSolicitacao.PENDENTE;
    private Status status = Status.ATIVO;

    @ManyToOne
    @JoinColumn(name = "idVenda")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "idConsumidor")
    private Consumidor consumidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idItemVenda", nullable = false)
    private ItemVenda itemVenda;

    @OneToMany(mappedBy = "solicitacao")
    private List<AnexoSolicitacao> anexos = new ArrayList<>();

    public enum TipoSolicitacao{
        TROCA,
        DEVOLUCAO;
    }
}
