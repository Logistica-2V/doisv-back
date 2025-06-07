package com.logistica.doisv.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.logistica.doisv.dto.ProdutoDTO;
import com.logistica.doisv.entities.Produto;
import com.logistica.doisv.repositories.ProdutoRepository;
import com.logistica.doisv.services.api.GoogleDriveService;
import com.logistica.doisv.services.exceptions.DatabaseException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository repository;

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> buscarTodos(Pageable pageable, Long idLoja){
        Page<Produto> produtos = repository.findAllByLoja_IdLoja(pageable, idLoja);
        return produtos.map( p -> new ProdutoDTO(p));
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id){
        Optional<Produto> resultado = repository.findById(id);
        Produto produto = resultado.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        ProdutoDTO dto = new ProdutoDTO(produto);
        return dto;
    }

    @Transactional
    public ProdutoDTO salvar(ProdutoDTO dto, MultipartFile imagem) throws IOException, GeneralSecurityException{
        Produto produto = new Produto();
        dtoParaEntidade(dto, produto);
        if(imagem.getContentType() != null){
            repository.save(produto);
            String url = GoogleDriveService.salvarArquivoDrive(imagem, produto.getIdProduto(), produto.getClass().getSimpleName());
            produto.setImagem(url.split("/")[5]);
        }
        return new ProdutoDTO(repository.save(produto));
    }

    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoDTO dto, Long idLoja, MultipartFile imagem) throws IOException, GeneralSecurityException{
        if(dto.getLoja().getIdLoja().equals(idLoja)){
            try {
                Produto produto = repository.getReferenceById(id);
                dtoParaEntidade(dto, produto);
                String url = GoogleDriveService.salvarArquivoDrive(imagem, produto.getIdProduto(), produto.getClass().getSimpleName());
                produto.setImagem(url.split("/")[5]);
                produto = repository.save(produto);
                return new ProdutoDTO(produto);
            }catch (EntityNotFoundException e){
                throw new ResourceNotFoundException("Produto não encontrado");
            }
        }
        throw new DataIntegrityViolationException(null);
    }

    @Transactional
    public void remover(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }catch(DataIntegrityViolationException e){
            throw new DatabaseException("Falha na integridade referencial");
        }
    }


    private void dtoParaEntidade(ProdutoDTO dto, Produto produto){
        produto.setDescricao(dto.getDescricao());
        produto.setUnidadeMedida(dto.getUnidadeMedida());
        produto.setPreco(dto.getPreco());
        produto.setStatusProduto(dto.getStatusProduto());
        produto.setLoja(dto.getLoja());
        produto.setImagem(dto.getImagem());
    }
}
