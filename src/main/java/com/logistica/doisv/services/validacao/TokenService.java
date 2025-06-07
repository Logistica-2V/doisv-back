package com.logistica.doisv.services.validacao;

import java.security.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.repositories.LojistaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
    
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private LojistaRepository repository;

    public AcessoDTO validarToken(String token){
        
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        
        try{
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
                Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build().parseClaimsJws(token)
                    .getBody();

            AcessoDTO acesso = new AcessoDTO(claims.get("idLojista",Long.class), 
                claims.get("sub", String.class), 
                claims.get("nomeLojista",String.class), 
                claims.get("idLoja", Long.class));

            if (!repository.existsByEmail(acesso.getEmailLojista())) {
                    throw new SecurityException("Lojista não localizado");
            }
            
            return acesso;

        }catch(Exception e){
            throw new SecurityException("Token inválido");
        }
    }      
}
