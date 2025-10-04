package com.logistica.doisv.repositories;

import com.logistica.doisv.entities.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    Page<Venda> findAllByLoja_idLoja(Pageable pageable, Long idLoja);
}
