package com.logistica.doisv.entities;

import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.entities.enums.StatusSolicitacao;
import com.logistica.doisv.entities.enums.TipoFeedback;
import com.logistica.doisv.services.exceptions.RegraNegocioException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_feedback")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFeedback;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoFeedback tipoFeedback;

    @Column(nullable = false)
    private Integer nota;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Column(nullable = false)
    private LocalDate dataFeedback = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idConsumidor", nullable = false)
    private Consumidor consumidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idLoja")
    private Loja loja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSolicitacao")
    private Solicitacao solicitacao;


    public static Feedback criar(Solicitacao solicitacao, Consumidor consumidor, Loja loja, TipoFeedback tipoFeedback,
                                 Integer nota, String comentario) {
        validarSolicitacaoConcluida(solicitacao);
        validarSeExisteFeedback(solicitacao, tipoFeedback);
        validarNota(nota);

        return Feedback.builder()
                .solicitacao(solicitacao)
                .tipoFeedback(tipoFeedback)
                .consumidor(consumidor)
                .loja(loja)
                .nota(nota)
                .comentario(comentario)
                .dataFeedback(LocalDate.now())
                .status(Status.ATIVO)
                .build();
    }

    private static void validarSolicitacaoConcluida(Solicitacao solicitacao){
        if(solicitacao.getStatusSolicitacao() != StatusSolicitacao.CONCLUIDA){
            throw new RegraNegocioException("A solicitação ainda está em andamento, aguarde ser concluída para dar um Feedback");
        }
    }

    private static void validarSeExisteFeedback(Solicitacao solicitacao, TipoFeedback tipoFeedback){
        boolean feedbackExistente = solicitacao.getFeedbacks().stream()
                .anyMatch(f -> f.getTipoFeedback().equals(tipoFeedback));

        if(feedbackExistente){
            throw new RegraNegocioException(String
                    .format("Já existe um Feedback para a solicitação ID %d", solicitacao.getId()));
        }
    }

    private static void validarNota(Integer nota) {
        if(nota < 1 || nota > 5){
            throw new RegraNegocioException("A nota deve ser entre 1 e 5.");
        }
    }
}
