package com.logistica.doisv.services;

import com.logistica.doisv.dto.LojistaDTO;
import com.logistica.doisv.entities.Lojista;
import com.logistica.doisv.repositories.LojistaRepository;
import com.logistica.doisv.services.exceptions.DatabaseException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LojistaService {

    @Autowired
    private LojistaRepository lojistaRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @Transactional(readOnly = true)
    public String login (String email, String password) {
        Lojista lojista = lojistaRepository.findByEmail(email);
        if (lojista != null && encoder.matches(password, lojista.getPassword())){
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
                return Jwts.builder()
                        .setSubject(email)
                        .claim("idLojista", lojista.getId())
                        .claim("nomeLojista",lojista.getNome())
                        .claim("idLoja", lojista.getLoja().getIdLoja())
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
        dtoParaEntidade(dto, lojista);
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
    public void deletar(Long id){
        if(!lojistaRepository.existsById(id)){
            throw new ResourceNotFoundException("Lojista não encontrado");
        }try {
            lojistaRepository.deleteById(id);
        }catch(DataIntegrityViolationException e){
            throw new DatabaseException("Falha na integridade referencial");
        }
    }


    public void dtoParaEntidade(LojistaDTO dto, Lojista lojista){
        lojista.setNome(dto.getNome());
        lojista.setCpf(dto.getCpf());
        lojista.setEmail(dto.getEmail());
        lojista.setPassword(encoder.encode(dto.getPassword()));
        lojista.setLoja(dto.getLoja());
    }
}
