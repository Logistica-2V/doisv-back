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
import com.logistica.doisv.util.geracao.GeradorSenhaAleatoria;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private EmailService emailService;

    @Autowired
    private PasswordEncoder encoder;

    @Value("${app.dominio.cnpj}")
    private String cnpjDominio;

    @Transactional(readOnly = true)
    public LojistaDTO buscarPorId(Long id, Long idLoja){
        return lojistaRepository.findByIdLojistaAndLojaIdLoja(id, idLoja)
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
    public LojistaDTO salvar(LojistaDTO dto, Long idLoja){
        Lojista lojista = new Lojista();
        Loja loja = lojaRepository.findById(idLoja).orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));

        dtoParaEntidade(dto, lojista);
        lojista.setLoja(loja);

        return new LojistaDTO(lojistaRepository.save(lojista));
    }

    @Transactional
    protected void cadastrarLojistaAdmin(Loja loja) throws MessagingException {
        String senhaInicial = GeradorSenhaAleatoria.gerarSenha();

        Lojista lojistaAdmin = gerarAcessoAdmin(loja, senhaInicial);

        lojistaRepository.save(lojistaAdmin);

        emailService.enviarEmailCadastroLoja(lojistaAdmin, senhaInicial);
    }

    @Transactional
    protected void cadastrarLojistaMaster(Loja loja) throws MessagingException {
        String senhaInicial = GeradorSenhaAleatoria.gerarSenha();

        Lojista lojistaMaster = gerarAcessoMaster(loja, senhaInicial);

        lojistaRepository.save(lojistaMaster);

        emailService.enviarEmailUsuarioMasterSuporte(lojistaMaster.getEmail(), senhaInicial, loja.getNome());
    }

    @Transactional
    public LojistaDTO atualizar(Long id, LojistaDTO dto, Long idLoja){
        Lojista lojista = lojistaRepository.findByIdLojistaAndLojaIdLoja(id, idLoja)
                    .orElseThrow(() -> new ResourceNotFoundException("Lojista não encontrado."));

        impedirEdicaoDeUsuarioPrivilegiado(lojista.getAdmin());

        dtoParaEntidade(dto, lojista);
        lojista = lojistaRepository.save(lojista);

        return new LojistaDTO(lojista);

    }

    @Transactional
    public void remover(Long id, Long idLoja){
        Lojista lojista = lojistaRepository.findByIdLojistaAndLojaIdLoja(id, idLoja)
                .orElseThrow(() -> new ResourceNotFoundException("Lojista não encontrado."));
        try {
            impedirEdicaoDeUsuarioPrivilegiado(lojista.getAdmin());
            lojistaRepository.delete(lojista);
        }catch(DataIntegrityViolationException e){
            throw new DatabaseException("Falha na integridade referencial");
        }
    }

    @Transactional
    public void inativar(List<Long> lojitasIds, Long idLoja){
        if(lojitasIds.isEmpty() || lojitasIds.contains(null)){
            throw new RegraNegocioException("Lista de lojistas vazia ou contém valor nulo");
        }

        var lojistas = lojistaRepository.findAllByIdLojistaInAndLojaIdLoja(lojitasIds, idLoja);

        if(lojistas.isEmpty()){
            throw new ResourceNotFoundException("Nenhum lojista encontrado para os IDs: " + lojitasIds);
        }
        lojistas.forEach(l -> l.setStatus(Status.INATIVO));
        lojistaRepository.saveAll(lojistas);
    }

    private void dtoParaEntidade(LojistaDTO dto, Lojista lojista){
        lojista.setNome(dto.nome());
        lojista.setCpf(dto.cpf());
        lojista.setEmail(dto.email());
        lojista.setPassword(encoder.encode(dto.password()));
        if (dto.status() != null && !dto.status().isBlank()) {
            lojista.setStatus(Status.converterParaString(dto.status()));
        }
    }

    private Lojista gerarAcessoAdmin(Loja loja, String senhaInicial){

        return Lojista.builder()
                .nome(loja.getNome())
                .email(loja.getEmail())
                .cpf(loja.getCnpj())
                .password(encoder.encode(senhaInicial))
                .status(Status.ATIVO)
                .admin(true)
                .loja(loja)
                .build();
    }

    private Lojista gerarAcessoMaster(Loja loja, String senhaInicial){
        return Lojista.builder()
                .nome(loja.getNome() + " MASTER")
                .email(loja.getCnpj() + "@doisv.com")
                .cpf(cnpjDominio)
                .password(encoder.encode(senhaInicial))
                .status(Status.ATIVO)
                .admin(true)
                .loja(loja)
                .build();
    }

    private void impedirEdicaoDeUsuarioPrivilegiado(boolean isUsuarioPrivilegiado){
        if(isUsuarioPrivilegiado){
            throw new RegraNegocioException("Não é possível editar ou excluir usuário ADMIN ou MASTER.");
        }
    }
}
