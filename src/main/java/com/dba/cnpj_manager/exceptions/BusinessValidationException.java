package com.dba.cnpj_manager.exceptions;

import java.util.HashMap;
import java.util.Map;

public class BusinessValidationException extends RuntimeException {

    public static final String FIELD_CNPJ = "cnpjCompleto";
    public static final String FIELD_RAZAO_SOCIAL = "razaoSocial";

    private final Map<String, String> errors = new HashMap<>();

    public BusinessValidationException(String message) {
        super(message);
    }

    // Método para adicionar erros um por um
    public void addError(String field, String message) {
        this.errors.put(field, message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}