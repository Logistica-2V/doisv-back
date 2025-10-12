package com.logistica.doisv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcessoDTO {

    private TipoUsuario tipo;
    private String subject;
    private String nome;
    private Long idLoja;

    private Long idLojista;
    private Long idConsumidor;
    private String serialVenda;
    private Long idVenda;

    public enum TipoUsuario{
        LOJISTA,
        CONSUMIDOR
    }

    public Collection<? extends GrantedAuthority> getPermissao(){
        if(this.tipo == null){
            return Collections.emptyList();
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.tipo.name()));
    }
}
