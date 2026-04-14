package com.dba.cnpj_manager.exceptions;

public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}