package com.logistica.doisv.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.logistica.doisv.entities.enums.CategoriaArquivoPermitida;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.services.exceptions.DatabaseException;
import com.logistica.doisv.util.validacao.ArquivoValidador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.logistica.doisv.dto.LojaDTO;
import com.logistica.doisv.entities.Loja;
import com.logistica.doisv.repositories.LojaRepository;
import com.logistica.doisv.services.api.GoogleDriveService;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LojaService {
    @Autowired
    private LojaRepository lojaRepository;

    @Autowired
    private ArquivoValidador arquivoValidador;

    @Transactional(readOnly = true)
    public LojaDTO buscarPorId(Long id) {
        return lojaRepository.findById(id)
                .map(LojaDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<LojaDTO> buscarTodos(){
        return lojaRepository.findAll()
                .stream().map(LojaDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public LojaDTO salvar(LojaDTO dto, MultipartFile logo) throws IOException, GeneralSecurityException {
        arquivoValidador.validarOpcional(logo, Set.of(CategoriaArquivoPermitida.IMAGEM));

        Loja loja = new Loja();
        dtoParaEntidade(dto, loja);

        if(logo != null && logo.getContentType() != null){
            lojaRepository.save(loja);
            String logoUrl = GoogleDriveService.salvarArquivoDrive(logo, loja.getIdLoja().toString(), loja.getClass().getSimpleName());
            loja.setLogo(logoUrl.split("/")[5]);
        }

        return new LojaDTO(lojaRepository.save(loja));
    }

    @Transactional
    public LojaDTO atualizar(Long id, LojaDTO dto, MultipartFile logo) throws IOException, GeneralSecurityException{
        try{
            Loja loja = lojaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loja não encontrado"));

            arquivoValidador.validarOpcional(logo, Set.of(CategoriaArquivoPermitida.IMAGEM));
            dtoParaEntidade(dto, loja);

            if(logo != null && logo.getContentType() != null){
                String logoUrl = GoogleDriveService.salvarArquivoDrive(logo, id.toString(), "Loja");
                loja.setLogo(logoUrl.split("/")[5]);
            }

            loja = lojaRepository.save(loja);

            return new LojaDTO(loja);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Loja não encontrado");
        }
    }

    //Necessário implementar futuramente uma deleção em massa de todos itens vinculados a loja
    @Transactional
    public void remover(Long id){
        if(!lojaRepository.existsById(id)){
            throw new ResourceNotFoundException("Loja não encontrada");
        }try {
            lojaRepository.deleteById(id);
        }catch(DataIntegrityViolationException e){
            throw new DatabaseException("Falha na integridade referencial");
        }
    }

    @Transactional
    public void inativar(Long id){
        Loja loja = lojaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));
        loja.setStatus(Status.INATIVO);
        lojaRepository.save(loja);
    }

    public void dtoParaEntidade(LojaDTO dto, Loja loja) {
        loja.setNome(dto.nome());
        loja.setEmail(dto.email());
        loja.setCnpj(dto.cnpj());
        loja.setSegmento(dto.segmento());
        if(dto.status() != null && !dto.status().isBlank()) {
            loja.setStatus(Status.converterParaString(dto.status()));
        }
    }
}
