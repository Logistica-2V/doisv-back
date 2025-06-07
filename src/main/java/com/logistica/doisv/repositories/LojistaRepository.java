package com.logistica.doisv.repositories;

import com.logistica.doisv.entities.Lojista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LojistaRepository extends JpaRepository<Lojista,Long> {
    Lojista findByEmail(String email);

    List<Lojista> findAllByLoja_IdLoja(Long idLoja);

    Boolean existsByEmail(String email);
}
