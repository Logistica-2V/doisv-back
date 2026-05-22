package com.logistica.doisv.modules.solicitacao.repository;

import com.logistica.doisv.modules.solicitacao.dto.SolicitacaoResumidaDTO;
import com.logistica.doisv.modules.solicitacao.entity.Solicitacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    @Query("""
    SELECT new com.logistica.doisv.modules.solicitacao.dto.SolicitacaoResumidaDTO(
        s.id, c.nome, v.id, p.descricao, s.tipoSolicitacao, s.motivo, s.observacao, s.dataSolicitacao,
        s.dataAtualizacao, s.statusSolicitacao, s.status)
        FROM Solicitacao s
        JOIN s.consumidor c
        JOIN s.venda v
        JOIN s.itemVenda iv
        JOIN iv.produto p
        WHERE v.loja.idLoja = :idLoja
    """)
    Page<SolicitacaoResumidaDTO> listarSolicitacoesResumidas(Pageable pageable, @Param("idLoja") Long idLoja);

    @Query("""
    SELECT new com.logistica.doisv.modules.solicitacao.dto.SolicitacaoResumidaDTO(
        s.id, c.nome, v.id, p.descricao, s.tipoSolicitacao, s.motivo, s.observacao, s.dataSolicitacao,
        s.dataAtualizacao, s.statusSolicitacao, s.status)
        FROM Solicitacao s
        JOIN s.consumidor c
        JOIN s.venda v
        JOIN s.itemVenda iv
        JOIN iv.produto p
        WHERE v.loja.idLoja = :idLoja
        AND v.id = :idVenda
    """)
    List<SolicitacaoResumidaDTO> listarSolicitacoesPorIdVendaEIdLoja(@Param("idVenda") Long idVenda,
                                                                     @Param("idLoja") Long idLoja);

    @Query("""
        SELECT DISTINCT s FROM Solicitacao s
        JOIN FETCH s.venda v
        JOIN FETCH v.loja l
        JOIN FETCH s.consumidor c
        JOIN FETCH s.itemVenda iv
        JOIN FETCH iv.produto p
        LEFT JOIN FETCH s.anexos
        LEFT JOIN FETCH s.historicos
        LEFT JOIN FETCH s.feedbacks
        WHERE s.id = :idSolicitacao
        AND l.idLoja = :idLoja
    """)
    Optional<Solicitacao> buscarCompletoPorId(@Param("idSolicitacao") Long idSolicitacao,
                                              @Param("idLoja") Long idLoja);


    @Query("""
            SELECT DISTINCT s FROM Solicitacao s
            JOIN FETCH s.venda v
            JOIN FETCH s.consumidor c
            JOIN FETCH s.itemVenda iv
            JOIN FETCH iv.produto p
            LEFT JOIN FETCH s.feedbacks f
            LEFT JOIN FETCH f.consumidor fc
            WHERE v.loja.idLoja = :idLoja
            AND FUNCTION('DATE', s.dataSolicitacao) BETWEEN :inicio AND :fim
            """)
    List<Solicitacao> buscarSolicitacaoPorLojaEPeriodo(@Param("idLoja") Long idLoja,
                                                       @Param("inicio") LocalDate inicio,
                                                       @Param("fim") LocalDate fim);

    @Query(value = """
    SELECT s FROM Solicitacao s
    LEFT JOIN FETCH s.feedbacks
    WHERE s.id = :idSolicitacao
    AND s.consumidor.idConsumidor = :idConsumidor
    AND s.venda.id = :idVenda
""")
    Optional<Solicitacao> buscarSolicitacaoPorIdEVendaEConsumidor(@Param("idSolicitacao") Long idSolicitacao,
                                                                  @Param("idConsumidor") Long idConsumidor,
                                                                  @Param("idVenda") Long idVenda);

    @Query("""
        SELECT s FROM Solicitacao s
        JOIN FETCH s.consumidor c
        JOIN FETCH s.venda v
        JOIN FETCH s.itemVenda iv
        JOIN FETCH iv.produto p
        WHERE v.loja.idLoja = :idLoja
    """)
    List<Solicitacao> buscarTodasPorLoja(@Param("idLoja") Long idLoja);
}
