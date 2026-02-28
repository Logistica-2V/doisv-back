package com.logistica.doisv.repositories;

import com.logistica.doisv.entities.Lojista;
import com.logistica.doisv.entities.enums.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LojistaRepository extends JpaRepository<Lojista,Long> {
    @EntityGraph(attributePaths = "loja")
    Optional<Lojista> findByEmail(String email);

    List<Lojista> findAllByLoja_IdLoja(Long idLoja);

    Optional<Lojista> findByIdLojistaAndLojaIdLoja(Long idLojista, Long idLoja);

    boolean existsByIdLojistaAndLojaIdLoja(Long idLojista, Long idLoja);

    List<Lojista> findAllByIdLojistaInAndLojaIdLoja(Iterable<Long> ids, Long idLoja);

    boolean existsByIdLojistaAndLoja_Status(Long idLojista, Status lojaStatus);
}
