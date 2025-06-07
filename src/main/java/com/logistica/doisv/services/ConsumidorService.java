package com.logistica.doisv.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logistica.doisv.dto.ConsumidorDTO;
import com.logistica.doisv.entities.Consumidor;
import com.logistica.doisv.repositories.ConsumidorRepository;
import com.logistica.doisv.services.exceptions.DatabaseException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ConsumidorService {
    @Autowired
    private ConsumidorRepository repository;

    @Transactional(readOnly = true)
    public List<ConsumidorDTO> buscarTodos(Long id){
        return repository.findAllByLoja_idLoja(id).stream().map(ConsumidorDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly=true)
    public ConsumidorDTO buscarPorId(Long id){
        Optional<Consumidor> resultado = repository.findById(id);
        return new ConsumidorDTO(resultado.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado")));
    }

    @Transactional
    public ConsumidorDTO salvar(ConsumidorDTO dto){
        Consumidor consumidor = new Consumidor();
        dtoParaEntidade(dto, consumidor);
        return new ConsumidorDTO(repository.save(consumidor));
    }

    @Transactional
    public ConsumidorDTO atualizar(ConsumidorDTO dto, Long id, Long idLoja){
        if(dto.getLoja().getIdLoja().equals(idLoja)){    
            try{
                Consumidor consumidor = repository.getReferenceById(id);
                dtoParaEntidade(dto, consumidor);
                return new ConsumidorDTO(repository.save(consumidor));
            }catch(EntityNotFoundException e){
                throw new ResourceNotFoundException("Recurso não encontrado");
            }
        }
            throw new DataIntegrityViolationException(null);
    }

    @Transactional
    public void remover(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha na integridade referencial");
        }
            
        
    }

    public void dtoParaEntidade(ConsumidorDTO dto, Consumidor consumidor){
        consumidor.setNome(dto.getNome());
        consumidor.setCpf_cnpj(dto.getCpf_cnpj());
        consumidor.setEmail(dto.getEmail());
        consumidor.setCelular(dto.getCelular());
        consumidor.setTelefone(dto.getTelefone());
        consumidor.setEndereco(dto.getEndereco());
        consumidor.setLoja(dto.getLoja());
    }
}
