package com.logistica.doisv.controllers.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.logistica.doisv.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerException {

    //Campos Nulos, Campos faltando, Sem Relacionamento -> 422  
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> ArgumentoInvalido(MethodArgumentNotValidException e, HttpServletRequest requisicao) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    //Recurso não encontrado -> 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> RecursoNaoEncontrado() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //Valores incorretos(tipo), tentar cadastrar item único mais de uma vez, relacionamento inexistente -> 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> ViolacaoDeIntegridade() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    //Requisição sem body -> 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> RequisicaoSemConteudo() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    //Requisição sem os parametros esperados -> 400
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> RequisicaoSemParametro() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
