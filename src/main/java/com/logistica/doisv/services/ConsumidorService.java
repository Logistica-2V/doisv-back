package com.logistica.doisv.services;

import com.logistica.doisv.dto.ConsumidorDTO;
import com.logistica.doisv.entities.Consumidor;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.entities.Venda;
import com.logistica.doisv.repositories.ConsumidorRepository;
import com.logistica.doisv.repositories.LojaRepository;
import com.logistica.doisv.repositories.VendaRepository;
import com.logistica.doisv.services.exceptions.AssociacaoInvalidaException;
import com.logistica.doisv.services.exceptions.DatabaseException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumidorService {
    @Autowired
    private ConsumidorRepository repository;

    @Autowired
    private LojaRepository lojaRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.consumidor.expiration}")
    private long validadeToken;

    @Transactional(readOnly = true)
    public String login(String serial, String senha){
        Venda venda = vendaRepository.findBySerialVendaIgnoreCase(serial).orElseThrow(() -> new ResourceNotFoundException("Venda não localizada"));
        if(venda != null && encoder.matches(senha,venda.getSenha())){
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

            Date dataCriacao = new Date();
            Date dataExpiracao = new Date(dataCriacao.getTime() + validadeToken);

            return Jwts.builder()
                    .setSubject(venda.getConsumidor().getEmail())
                    .claim("tipo", "CONSUMIDOR")
                    .claim("idConsumidor", venda.getConsumidor().getIdConsumidor())
                    .claim("nome", venda.getConsumidor().getNome())
                    .claim("serialVenda", venda.getSerialVenda())
                    .claim("idVenda", venda.getId())
                    .claim("idLoja", venda.getLoja().getIdLoja())
                    .setIssuedAt(dataCriacao)
                    .setExpiration(dataExpiracao)
                    .signWith(key)
                    .compact();
        }
        return null;
    }

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
            throw new AssociacaoInvalidaException("Você não tem permissão para editar um ou mais consumidores desta lista.");
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
        if(dto.status() != null && !dto.status().isBlank()){
            consumidor.setStatus(Status.converterParaString(dto.status()));
        }
    }

    private void validarLojaConsumidor(Long idLoja, Consumidor consumidor){
        if(!consumidor.getLoja().getIdLoja().equals(idLoja)) {
            throw new AssociacaoInvalidaException("Você não tem permissão para editar esse produto");
        }
    }
}
