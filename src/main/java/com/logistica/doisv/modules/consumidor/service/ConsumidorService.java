package com.logistica.doisv.modules.consumidor.service;

import com.logistica.doisv.modules.consumidor.dto.ConsumidorDTO;
import com.logistica.doisv.modules.consumidor.entity.Consumidor;
import com.logistica.doisv.modules.loja.entity.Loja;
import com.logistica.doisv.core.enums.Status;
import com.logistica.doisv.modules.consumidor.repository.ConsumidorRepository;
import com.logistica.doisv.modules.loja.repository.LojaRepository;
import com.logistica.doisv.core.exception.DatabaseException;
import com.logistica.doisv.core.exception.RegraNegocioException;
import com.logistica.doisv.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsumidorService {
    @Autowired
    private ConsumidorRepository repository;

    @Autowired
    private LojaRepository lojaRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.consumidor.expiration}")
    private long validadeToken;

    @Transactional(readOnly = true)
    public List<ConsumidorDTO> buscarTodos(Long idLoja){
        return repository.findAllByLoja_idLoja(idLoja).stream()
                .map(ConsumidorDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly=true)
    public ConsumidorDTO buscarPorId(Long idConsumidor, Long idLoja){
        Consumidor consumidor = repository.findByIdConsumidorAndLojaIdLoja(idConsumidor, idLoja)
                .orElseThrow(() -> new ResourceNotFoundException("Consumidor não encontrado"));

        return new ConsumidorDTO(consumidor);
    }

    @Transactional
    public ConsumidorDTO salvar(ConsumidorDTO dto, Long idLoja){
        Consumidor consumidor = new Consumidor();
        dtoParaEntidade(dto, consumidor);

        Loja loja = lojaRepository.findById(idLoja)
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));
        
        consumidor.setLoja(loja);
        return new ConsumidorDTO(repository.save(consumidor));
    }

    @Transactional
    public ConsumidorDTO atualizar(ConsumidorDTO dto, Long idConsumidor, Long idLoja){
        Consumidor consumidor = repository.findByIdConsumidorAndLojaIdLoja(idConsumidor, idLoja)
                .orElseThrow(() -> new ResourceNotFoundException("Consumidor não encontrado"));

        dtoParaEntidade(dto, consumidor);
        return new ConsumidorDTO(repository.save(consumidor));
    }

    @Transactional
    public void remover(Long idConsumidor, Long idLoja){
        Consumidor consumidor = repository.findByIdConsumidorAndLojaIdLoja(idConsumidor, idLoja)
                .orElseThrow(() -> new ResourceNotFoundException("Consumidor não encontrado"));

        try {
            repository.delete(consumidor);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha na integridade referencial");
        }
    }

    @Transactional
    public void inativar(List<Long> idsConsumidores, Long idLoja){
        int quantidadeConsumidoresInativados = repository
                .atualizarStatusConsumidores(idsConsumidores, idLoja, Status.INATIVO);

        if(quantidadeConsumidoresInativados <= 0){
            throw new RegraNegocioException("Nenhum consumidor encontrado para inativação com os IDs informados para esta loja.");
        }
    }

    public void dtoParaEntidade(ConsumidorDTO dto, Consumidor consumidor){
        consumidor.setNome(dto.nome());
        consumidor.setCpf_cnpj(dto.cpf_cnpj());
        consumidor.setEmail(dto.email());
        consumidor.setCelular(somenteNumeros(dto.celular()));
        consumidor.setTelefone(somenteNumeros(dto.telefone()));
        consumidor.setEndereco(dto.endereco());
        if(dto.status() != null && !dto.status().isBlank()){
            consumidor.setStatus(Status.converterStringParaEnum(dto.status()));
        }
    }

    private String somenteNumeros(String valor) {
        if (valor == null) {
            return null;
        }
        return valor.replaceAll("\\D", "");
    }
}
