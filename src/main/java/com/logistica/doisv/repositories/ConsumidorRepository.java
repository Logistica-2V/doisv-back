package com.logistica.doisv.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.logistica.doisv.entities.Consumidor;

@Repository
public interface ConsumidorRepository extends JpaRepository<Consumidor,Long>{
    List<Consumidor> findAllByLoja_idLoja(Long idLoja);
}
