package com.logistica.doisv.repositories;

import com.logistica.doisv.entities.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findAllByLoja_IdLoja(Pageable pageable, Long idLoja);

    Optional<Produto> findByIdProdutoAndLoja_IdLoja(Long idProduto, Long lojaIdLoja);
}
