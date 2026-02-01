package com.logistica.doisv.repositories;

import com.logistica.doisv.entities.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    Page<Venda> findAllByLoja_idLoja(Pageable pageable, Long idLoja);

    Optional<Venda> findBySerialVendaIgnoreCase(String serialVenda);

    @Query("""
            SELECT v FROM Venda v
            JOIN FETCH v.itensVenda i
            WHERE v.id = :id
            """)
    Optional<Venda> buscarVendaPorId(@Param("id")Long id);
}
