package com.logistica.doisv.services;

import com.logistica.doisv.dto.ProdutoDTO;
import com.logistica.doisv.entities.Produto;
import com.logistica.doisv.repositories.LojaRepository;
import com.logistica.doisv.repositories.ProdutoRepository;
import com.logistica.doisv.services.api.GoogleDriveService;
import com.logistica.doisv.services.exceptions.DatabaseException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private LojaRepository lojaRepository;

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> buscarTodos(Pageable pageable, Long idLoja){
        Page<Produto> produtos = repository.findAllByLoja_IdLoja(pageable, idLoja);
        return produtos.map(ProdutoDTO::new);
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id){
        Optional<Produto> resultado = repository.findById(id);
        Produto produto = resultado.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return new ProdutoDTO(produto);
    }

    @Transactional
    public ProdutoDTO salvar(ProdutoDTO dto, MultipartFile imagem, Long idLoja) throws IOException, GeneralSecurityException{
        Produto produto = new Produto();
        dtoParaEntidade(dto, produto);
        produto.setLoja(lojaRepository.findById(idLoja).orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada")));

        if(imagem.getContentType() != null){
            repository.save(produto);
            String url = GoogleDriveService.salvarArquivoDrive(imagem, produto.getIdProduto(), produto.getClass().getSimpleName());
            produto.setImagem(url.split("/")[5]);
        }
        return new ProdutoDTO(repository.save(produto));
    }

    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoDTO dto, Long idLoja, MultipartFile imagem) throws IOException, GeneralSecurityException{
        Produto produto = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if(!produto.getLoja().getIdLoja().equals(idLoja)) {
            throw new AccessDeniedException("Você não tem permissão para editar esse produto");
        }
        dtoParaEntidade(dto, produto);
        String url = GoogleDriveService.salvarArquivoDrive(imagem, produto.getIdProduto(), produto.getClass().getSimpleName());
        produto.setImagem(url.split("/")[5]);
        produto = repository.save(produto);
        return new ProdutoDTO(produto);
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
        produto.setDescricao(dto.descricao());
        produto.setUnidadeMedida(dto.unidadeMedida());
        produto.setPreco(dto.preco());
        produto.setImagem(dto.imagem());
    }
}
