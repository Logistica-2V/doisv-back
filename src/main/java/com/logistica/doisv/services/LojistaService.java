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
import com.logistica.doisv.services.exceptions.UsuarioInativoException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
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

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.lojista.expiration}")
    private long validadeToken;

    @Transactional(readOnly = true)
    public String login (String email, String password) {
        Lojista lojista = lojistaRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if(!lojista.getStatus().equals(Status.ATIVO)){
            throw new UsuarioInativoException("Usuário inativo, entre em contato com o admin para reativa-lo.");
        }

        if (encoder.matches(password, lojista.getPassword())){
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

            Date dataCriacao = new Date();
            Date dataExpiracao = new Date(dataCriacao.getTime() + validadeToken);

                return Jwts.builder()
                        .setSubject(email)
                        .claim("tipo", "LOJISTA")
                        .claim("idLojista", lojista.getIdLojista())
                        .claim("nome",lojista.getNome())
                        .claim("idLoja", lojista.getLoja().getIdLoja())
                        .setIssuedAt(dataCriacao)
                        .setExpiration(dataExpiracao)
                        .signWith(key)
                        .compact();
        }
        return null;
    }

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
