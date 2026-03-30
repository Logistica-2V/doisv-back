package com.logistica.doisv.repositories;

import com.logistica.doisv.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("""
            SELECT f FROM Feedback f
            JOIN FETCH f.consumidor
            JOIN FETCH f.solicitacao s
            JOIN FETCH s.venda v
            JOIN FETCH f.loja l
            WHERE f.solicitacao.id = :idSolicitacao
            AND f.loja.idLoja = :idLoja
            """)
    List<Feedback> buscarFeedbacksPorIdSolicitacao(@Param("idSolicitacao") Long idSolicitacao,
                                                   @Param("idLoja") Long idLoja);

    @Query("""
            SELECT f FROM Feedback f
            JOIN FETCH f.consumidor c
            WHERE f.idFeedback = :idFeedback
            AND f.loja.idLoja = :idLoja
            """)
    Optional<Feedback> buscarFeedbackPorId(@Param("idFeedback") Long idFeedback,
                                           @Param("idLoja") Long idLoja);

    @Query("""
        SELECT f FROM Feedback f
        JOIN FETCH f.solicitacao s
        JOIN FETCH s.venda
        WHERE f.loja.idLoja = :idLoja
        AND f.dataFeedback BETWEEN :inicio and :fim
        """)
    List<Feedback> buscarFeedbacksPorLojaEPeriodo(@Param("idLoja") Long idLoja,
                                                  @Param("inicio") LocalDate inicio,
                                                  @Param("fim") LocalDate fim);

    @Query("""
        SELECT f FROM Feedback f
        JOIN FETCH f.solicitacao s
        JOIN FETCH s.venda
        JOIN FETCH f.loja
        JOIN FETCH f.consumidor
        WHERE f.loja.idPublico = :idPublicoLoja
        AND f.dataFeedback BETWEEN :inicio and :fim
        """)
    List<Feedback> buscarFeedbacksPublicosPorLojaEPeriodo(@Param("idPublicoLoja") UUID idPublicoLoja,
                                                  @Param("inicio") LocalDate inicio,
                                                  @Param("fim") LocalDate fim);

    @Query("""
       SELECT f FROM Feedback f
       JOIN FETCH f.loja
       WHERE f.dataFeedback BETWEEN :inicio AND :fim
       """)
    List<Feedback> buscarFeedbacksPorPeriodo(LocalDate inicio, LocalDate fim);

}
