package com.logistica.doisv.modules.loja.repository;

import com.logistica.doisv.modules.loja.entity.Loja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LojaRepository extends JpaRepository<Loja, Long> {
    Page<Loja> findAll(Pageable pageable);
}
