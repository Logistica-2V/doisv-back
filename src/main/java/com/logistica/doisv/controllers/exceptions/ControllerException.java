package com.logistica.doisv.controllers.exceptions;

import com.logistica.doisv.dto.ErroCustomizado;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingMatrixVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.logistica.doisv.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;

@ControllerAdvice
public class ControllerException {

    //Campos Nulos, Campos faltando, Sem Relacionamento -> 422  
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> ArgumentoInvalido(MethodArgumentNotValidException e, HttpServletRequest requisicao) {
        ErroCustomizado erro = new ErroCustomizado(Instant.now(), HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage(), requisicao.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
    }

    //Recurso não encontrado -> 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> RecursoNaoEncontrado(ResourceNotFoundException e, HttpServletRequest requisicao) {
        ErroCustomizado erro = new ErroCustomizado(Instant.now(), HttpStatus.NOT_FOUND.value(), e.getMessage(), requisicao.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    //Valores incorretos(tipo), tentar cadastrar item único mais de uma vez, relacionamento inexistente -> 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> ViolacaoDeIntegridade(DataIntegrityViolationException e, HttpServletRequest requisicao) {
        ErroCustomizado erro = new ErroCustomizado(Instant.now(), HttpStatus.CONFLICT.value(), e.getMessage(), requisicao.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    //Requisição sem body -> 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> RequisicaoSemConteudo(HttpMessageNotReadableException e, HttpServletRequest requisicao) {
        ErroCustomizado erro = new ErroCustomizado(Instant.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage(), requisicao.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);

    }

    //Requisição sem os parametros esperados -> 400
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> RequisicaoSemParametro(MissingMatrixVariableException e, HttpServletRequest requisicao) {
        ErroCustomizado erro = new ErroCustomizado(Instant.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage(), requisicao.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}
