package com.dba.cnpj_manager.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record StandardErrorResponse(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<FieldError> fieldErrors // Preenchido apenas quando houver erro de validação de DTO
) {
    public StandardErrorResponse {
        if (fieldErrors == null) {
            fieldErrors = List.of();
        }
    }

    // Sub-record para mapear exatamente qual campo falhou e por quê
    public record FieldError(String field, String message) {
    }
}