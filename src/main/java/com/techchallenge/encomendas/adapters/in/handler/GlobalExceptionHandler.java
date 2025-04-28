package com.techchallenge.encomendas.adapters.in.handler;

import com.techchallenge.encomendas.domain.exceptions.CamposObrigatoriosException;
import com.techchallenge.encomendas.domain.exceptions.encomenda.EncomendaJaRetiradaException;
import com.techchallenge.encomendas.domain.exceptions.encomenda.EncomendaNaoEncontradaException;
import com.techchallenge.encomendas.domain.exceptions.morador.MoradorJaCadastradoException;
import com.techchallenge.encomendas.domain.exceptions.morador.MoradorNaoEncontradoException;
import com.techchallenge.encomendas.domain.exceptions.morador.MoradorNaoEncontradoApartamentoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MoradorNaoEncontradoException.class)
    public ResponseEntity<?> handleMoradorNaoEncontrado(MoradorNaoEncontradoException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MoradorJaCadastradoException.class)
    public ResponseEntity<?> handleMoradorJaCadastrado(MoradorJaCadastradoException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MoradorNaoEncontradoApartamentoException.class)
    public ResponseEntity<?> handleMoradorNaoEncontradoApartamentoCadastrado(MoradorNaoEncontradoApartamentoException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(buildResponse("Não encontrado"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EncomendaNaoEncontradaException.class)
    public ResponseEntity<?> handleEncomendaNaoEncontrada(EncomendaNaoEncontradaException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EncomendaJaRetiradaException.class)
    public ResponseEntity<?> handleEncomendaJaRetirada(EncomendaJaRetiradaException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CamposObrigatoriosException.class)
    public ResponseEntity<?> handleCamposObrigatorios(CamposObrigatoriosException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return new ResponseEntity<>(buildResponse("Erro interno no servidor: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> buildResponse(String mensagem) {
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        var dataFormatada = LocalDateTime.now().format(formatter);

        return Map.of(
                "mensagem", mensagem,
                "timestamp", dataFormatada
        );
    }
}
