package com.logistica.doisv.entities;

import com.logistica.doisv.entities.enums.StatusSolicitacao;
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
    private StatusSolicitacao statusAnterior;
    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusAtual;
    private LocalDateTime dataAtualizacao = LocalDateTime.now();
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
                .dataAtualizacao(LocalDateTime.now())
                .build();
    }

    public static HistoricoSolicitacao aprovacao(Solicitacao solicitacao) {
        String mensagem = String.format("Solicitação de %s aprovada!",
                solicitacao.getTipoSolicitacao().getDescricao().toLowerCase());
        return criar(StatusSolicitacao.APROVADA, solicitacao, mensagem);
    }

    public static HistoricoSolicitacao rejeicao(Solicitacao solicitacao) {
        String mensagem = String.format("Solicitação de %s reprovada!",
                solicitacao.getTipoSolicitacao().getDescricao().toLowerCase());
        return criar(StatusSolicitacao.REJEITADA, solicitacao, mensagem);
    }

    public static HistoricoSolicitacao cancelamento(Solicitacao solicitacao) {
        String mensagem = String.format("Solicitação de %s cancelada.",
                solicitacao.getTipoSolicitacao().getDescricao().toLowerCase());
        return criar(StatusSolicitacao.CANCELADA, solicitacao, mensagem);
    }

}
