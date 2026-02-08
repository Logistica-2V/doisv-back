package com.logistica.doisv.services;

import com.logistica.doisv.dto.ConsumidorLoginDTO;
import com.logistica.doisv.entities.Lojista;
import com.logistica.doisv.entities.RecuperarSenha;
import com.logistica.doisv.entities.Venda;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.repositories.LojaRepository;
import com.logistica.doisv.repositories.LojistaRepository;
import com.logistica.doisv.repositories.RecuperarSenhaRepository;
import com.logistica.doisv.repositories.VendaRepository;
import com.logistica.doisv.services.exceptions.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.Random;

@Service
public class AutenticacaoService {
    @Autowired
    private LojistaRepository lojistaRepository;

    @Autowired
    private LojaRepository lojaRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private RecuperarSenhaRepository recuperarSenhaRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.lojista.expiration}")
    private long validadeToken;

    @Transactional(readOnly = true)
    public String loginLojista(String email, String password) {
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

    @Transactional
    public void recuperarSenhaLojista(String email) throws MessagingException {
        Lojista lojista = buscarLojistaAtivo(email);

        String codigoRecuperacao = gerarCodigoRecuperacao();

        cancelarRecuperacoesAnteriores(email, "0");

        recuperarSenhaRepository.save(new RecuperarSenha(codigoRecuperacao, lojista));

        emailService.enviarEmailRecuperacaoSenha(lojista, codigoRecuperacao);
    }

    @Transactional
    public void validarCodigoRecuperacao(String email, String codigoRecuperacao){
        RecuperarSenha recuperacao = validarRecuperacao(email, codigoRecuperacao);

        cancelarRecuperacoesAnteriores(email, codigoRecuperacao);
    }

    @Transactional
    public void atualizarSenha(String email, String codigoRecuperacao, String novaSenha){
        RecuperarSenha recuperacao = validarRecuperacao(email, codigoRecuperacao);

        validarForcaSenha(novaSenha);
        
        recuperacao.getLojista().setPassword(encoder.encode(novaSenha));

        recuperacao.setStatus(Status.INATIVO);
    }

    @Transactional(readOnly = true)
    public String loginConsumidor(ConsumidorLoginDTO dto){
        Venda venda = vendaRepository.findBySerialVendaIgnoreCase(dto.serial())
                .orElseThrow(() -> new ResourceNotFoundException("Venda não localizada"));

        if(venda != null && encoder.matches(dto.senha(),venda.getSenha())){
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

    @Transactional
    private void cancelarRecuperacoesAnteriores(String email, String codigoRecuperacao){
        recuperarSenhaRepository.cancelarCodigoRecuperacaoAnteriores(email, codigoRecuperacao);
    }

    private String gerarCodigoRecuperacao(){
        Random random = new Random();

        return String.format("%06d", random.nextInt(900000) + 100000);
    }

    private Lojista buscarLojistaAtivo(String email){
        Lojista lojista = lojistaRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Lojista não encontrado com este email."));

        if(lojista.getStatus().equals(Status.INATIVO)){
            throw new UsuarioInativoException("Não é possível recuperar senha de um usuário inativo, entre em contato com o administrador para reativa-lo.");
        }

        return lojista;
    }

    private RecuperarSenha validarRecuperacao(String email, String codigoRecuperacao){
        RecuperarSenha recuperacao = recuperarSenhaRepository.validarRecuperacao(email, codigoRecuperacao)
                .orElseThrow(() -> new ResourceNotFoundException("Código de recuperação inválido"));

        if (recuperacao.isExpirado()) {
            throw new CodigoExpiradoException("Código de recuperação expirado");
        }

        if (recuperacao.getStatus().equals(Status.INATIVO)) {
            throw new RegraNegocioException("Este código já foi utilizado");
        }

        return recuperacao;
    }

    private void validarForcaSenha(String senha) {
        if (senha == null || senha.length() < 8) {
            throw new SenhaFracaException("A senha deve ter no mínimo 8 caracteres");
        }
    }
}
