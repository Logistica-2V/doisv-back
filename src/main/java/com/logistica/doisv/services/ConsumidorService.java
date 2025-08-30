package com.logistica.doisv.services;

import com.logistica.doisv.dto.ConsumidorDTO;
import com.logistica.doisv.entities.Consumidor;
import com.logistica.doisv.entities.Status;
import com.logistica.doisv.repositories.ConsumidorRepository;
import com.logistica.doisv.repositories.LojaRepository;
import com.logistica.doisv.services.exceptions.DatabaseException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumidorService {
    @Autowired
    private ConsumidorRepository repository;

    @Autowired
    private LojaRepository lojaRepository;

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
    public ConsumidorDTO salvar(ConsumidorDTO dto, Long idLoja){
        Consumidor consumidor = new Consumidor();
        dtoParaEntidade(dto, consumidor);
        consumidor.setLoja(lojaRepository.findById(idLoja).orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada")));
        return new ConsumidorDTO(repository.save(consumidor));
    }

    @Transactional
    public ConsumidorDTO atualizar(ConsumidorDTO dto, Long id, Long idLoja){
        Consumidor consumidor = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Consumidor não encontrado"));
        if(!consumidor.getLoja().getIdLoja().equals(idLoja)){
            throw new AccessDeniedException("Você não tem permissão para editar esse produto");
        }
        dtoParaEntidade(dto, consumidor);
        return new ConsumidorDTO(repository.save(consumidor));
    }

    @Transactional
    public void remover(Long id, Long idLoja){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            validarLojaConsumidor(idLoja, repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Consumidor não encontrado")));
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha na integridade referencial");
        }
    }

    @Transactional
    public void inativar(List<Long> ids, Long idLoja){
        List<Consumidor> consumidores = repository.findAllById(ids);
        if(consumidores.stream().anyMatch(c -> !c.getLoja().getIdLoja().equals(idLoja))){
            throw new AccessDeniedException("Você não tem permissão para editar um ou mais consumidores desta lista.");
        }
        consumidores.forEach(c -> c.setStatus(Status.INATVO));
        repository.saveAll(consumidores);
    }

    public void dtoParaEntidade(ConsumidorDTO dto, Consumidor consumidor){
        consumidor.setNome(dto.nome());
        consumidor.setCpf_cnpj(dto.cpf_cnpj());
        consumidor.setEmail(dto.email());
        consumidor.setCelular(dto.celular());
        consumidor.setTelefone(dto.telefone());
        consumidor.setEndereco(dto.endereco());
    }

    private void validarLojaConsumidor(Long idLoja, Consumidor consumidor){
        if(!consumidor.getLoja().getIdLoja().equals(idLoja)) {
            throw new AccessDeniedException("Você não tem permissão para editar esse produto");
        }
    }
}
