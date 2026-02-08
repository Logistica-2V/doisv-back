package com.logistica.doisv.services;

import com.logistica.doisv.dto.LojistaDTO;
import com.logistica.doisv.entities.Loja;
import com.logistica.doisv.entities.Lojista;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.repositories.LojaRepository;
import com.logistica.doisv.repositories.LojistaRepository;
import com.logistica.doisv.services.exceptions.DatabaseException;
import com.logistica.doisv.services.exceptions.RegraNegocioException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LojistaService {

    @Autowired
    private LojistaRepository lojistaRepository;

    @Autowired
    private LojaRepository lojaRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public LojistaDTO buscarPorId(Long id){
        return lojistaRepository.findById(id)
                .map(LojistaDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Lojista não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<LojistaDTO> buscarLojistaPorLoja(Long id){
        return lojistaRepository.findAllByLoja_IdLoja(id)
                .stream().map(LojistaDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LojistaDTO> buscarTodos(){
        return lojistaRepository.findAll()
                .stream().map(LojistaDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public LojistaDTO salvar(LojistaDTO dto){
        Lojista lojista = new Lojista();
        Loja loja = lojaRepository.findById(dto.idLoja()).orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));
        dtoParaEntidade(dto, lojista);
        lojista.setLoja(loja);
        return new LojistaDTO(lojistaRepository.save(lojista));
    }

    @Transactional
    public LojistaDTO atualizar(Long id, LojistaDTO dto){
        try {
            Lojista lojista = lojistaRepository.getReferenceById(id);
            dtoParaEntidade(dto, lojista);
            lojista = lojistaRepository.save(lojista);
            return new LojistaDTO(lojista);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Lojista não encontrado");
        }
    }

    @Transactional
    public void remover(Long id){
        if(!lojistaRepository.existsById(id)){
            throw new ResourceNotFoundException("Lojista não encontrado");
        }try {
            lojistaRepository.deleteById(id);
        }catch(DataIntegrityViolationException e){
            throw new DatabaseException("Falha na integridade referencial");
        }
    }

    @Transactional
    public void inativar(List<Long> lojitasIds){
        if(lojitasIds.isEmpty() || lojitasIds.contains(null)){
            throw new RegraNegocioException("Lista de lojistas vazia ou contém valor nulo");
        }

        var lojistas = lojistaRepository.findAllById(lojitasIds);
        lojistas.forEach(l -> l.setStatus(Status.INATIVO));
        lojistaRepository.saveAll(lojistas);
    }

    public void dtoParaEntidade(LojistaDTO dto, Lojista lojista){
        lojista.setNome(dto.nome());
        lojista.setCpf(dto.cpf());
        lojista.setEmail(dto.email());
        lojista.setPassword(encoder.encode(dto.password()));
        if (dto.status() != null && !dto.status().isBlank()) {
            lojista.setStatus(Status.converterParaString(dto.status()));
        }
    }
}
