package com.dba.cnpj_manager.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // Status 404
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<StandardErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                StandardErrorResponse response = new StandardErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                "Not Found",
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Status 422 ou 400
        @ExceptionHandler(BusinessValidationException.class)
        public ResponseEntity<StandardErrorResponse> handleBusinessValidation(BusinessValidationException ex,
                        HttpServletRequest request) {

                List<StandardErrorResponse.FieldError> fieldErrors = ex.getErrors().entrySet().stream()
                                .map(entry -> new StandardErrorResponse.FieldError(entry.getKey(), entry.getValue()))
                                .toList();

                StandardErrorResponse response = new StandardErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                                "Unprocessable Content",
                                ex.getMessage(),
                                request.getRequestURI(),
                                fieldErrors.isEmpty() ? null : fieldErrors);

                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(response);
        }

        // Status 400
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<StandardErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                List<StandardErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> new StandardErrorResponse.FieldError(error.getField(),
                                                error.getDefaultMessage()))
                                .toList();

                StandardErrorResponse response = new StandardErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Error",
                                "Erro na validação dos campos da requisição.",
                                request.getRequestURI(),
                                fieldErrors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(TokenGenerationException.class)
        public ResponseEntity<StandardErrorResponse> handleTokenGeneration(TokenGenerationException ex,
                        HttpServletRequest request) {
                ex.printStackTrace();

                StandardErrorResponse response = new StandardErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Security Error",
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<StandardErrorResponse> handleBadCredentials(BadCredentialsException ex,
                        HttpServletRequest request) {

                StandardErrorResponse response = new StandardErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.UNAUTHORIZED.value(),
                                "Authentication Error",
                                "Usuário ou senha inválidos.",
                                request.getRequestURI(),
                                null);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Status 500
        @ExceptionHandler(Exception.class)
        public ResponseEntity<StandardErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
                ex.printStackTrace();

                StandardErrorResponse response = new StandardErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                "Ocorreu um erro inesperado no servidor.",
                                request.getRequestURI(),
                                null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
}