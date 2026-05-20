package com.logistica.doisv.modules.solicitacao.entity;

import com.logistica.doisv.core.enums.StatusSolicitacao;
import com.logistica.doisv.core.util.generation.DataUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_Historico_Solicitacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoSolicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSolicitacao statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSolicitacao statusAtual;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao = DataUtil.dataHoraAgora();

    @Column(nullable = false, columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "idSolicitacao", nullable = false)
    private Solicitacao solicitacao;

    public static HistoricoSolicitacao criar(StatusSolicitacao status,
                                             Solicitacao solicitacao,
                                             String mensagem) {
        return HistoricoSolicitacao.builder()
                .statusAnterior(solicitacao.getStatusSolicitacao())
                .statusAtual(status)
                .observacao(mensagem)
                .solicitacao(solicitacao)
                .dataAtualizacao(DataUtil.dataHoraAgora())
                .build();
    }

    public static HistoricoSolicitacao aprovacao(Solicitacao solicitacao) {
        String mensagem = String.format("Solicitação de %s aprovada!",
                solicitacao.getTipoSolicitacao().getDescricao().toLowerCase());
        return criar(StatusSolicitacao.APROVADA, solicitacao, mensagem);
    }

    public static HistoricoSolicitacao rejeicao(Solicitacao solicitacao, String motivoReprovacao) {
        String mensagem = String.format("Solicitação de %s reprovada!",
                solicitacao.getTipoSolicitacao().getDescricao().toLowerCase());
        if (motivoReprovacao != null && !motivoReprovacao.trim().isEmpty()) {
            mensagem += " Motivo: " + motivoReprovacao;
        }
        return criar(StatusSolicitacao.REJEITADA, solicitacao, mensagem);
    }

    public static HistoricoSolicitacao cancelamento(Solicitacao solicitacao) {
        String mensagem = String.format("Solicitação de %s cancelada.",
                solicitacao.getTipoSolicitacao().getDescricao().toLowerCase());
        return criar(StatusSolicitacao.CANCELADA, solicitacao, mensagem);
    }

}
