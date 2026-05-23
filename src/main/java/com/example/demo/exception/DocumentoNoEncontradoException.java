package com.example.demo.exception;

public class DocumentoNoEncontradoException extends RuntimeException {
    public DocumentoNoEncontradoException(String message) {
        super(message);
    }
}
