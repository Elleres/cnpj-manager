package com.dba.cnpj_manager.exceptions;

// Usada estritamente quando um ID buscado no banco não existe.
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
