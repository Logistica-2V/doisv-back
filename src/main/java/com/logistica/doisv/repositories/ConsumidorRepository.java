package com.logistica.doisv.repositories;

import com.logistica.doisv.entities.Consumidor;
import com.logistica.doisv.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumidorRepository extends JpaRepository<Consumidor,Long>{
    List<Consumidor> findAllByLoja_idLoja(Long idLoja);

    Optional<Consumidor> findByIdConsumidorAndStatusAndLojaIdLoja(Long idConsumidor, Status status, Long idLoja);

    default Optional<Consumidor> buscarConsumidorAtivo(Long idConsumidor, Long idLoja){
        return findByIdConsumidorAndStatusAndLojaIdLoja(idConsumidor, Status.ATIVO, idLoja);
    }
}
