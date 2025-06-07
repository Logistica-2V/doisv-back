package com.logistica.doisv.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
        Loja loja = new Loja();
        dtoParaEntidade(dto, loja);
        if(logo.getContentType() != null){
            lojaRepository.save(loja);
            String logoUrl = GoogleDriveService.salvarArquivoDrive(logo, loja.getIdLoja(), loja.getClass().getSimpleName());
            loja.setLogo(logoUrl.split("/")[5]);
        }
        return new LojaDTO(lojaRepository.save(loja));
    }

    @Transactional
    public LojaDTO atualizar(Long id, LojaDTO dto, MultipartFile logo) throws IOException, GeneralSecurityException{
        try{
            Loja loja = lojaRepository.getReferenceById(id);
            dtoParaEntidade(dto, loja);
            String logoUrl = GoogleDriveService.salvarArquivoDrive(logo, id, loja.getClass().getSimpleName());
            loja.setLogo(logoUrl.split("/")[5]);
            loja = lojaRepository.save(loja);
            return new LojaDTO(loja);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Produto não encontrado");
        }
    }

    public void dtoParaEntidade(LojaDTO lojaDTO, Loja loja) {
        loja.setNome(lojaDTO.getNome());
        loja.setEmail(lojaDTO.getEmail());
        loja.setCnpj(lojaDTO.getCnpj());
        loja.setLogo(lojaDTO.getLogo());
        loja.setSegmento(lojaDTO.getSegmento());
    }
}
