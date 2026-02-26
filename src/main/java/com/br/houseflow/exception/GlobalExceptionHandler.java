package com.br.houseflow.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //  404 - user/task não encontrado
    @ExceptionHandler({UserNotFoundException.class, TaskNotFoundException.class})
    public ResponseEntity<StandardError> handleNotFound(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //  403 - regra de negócio: sem permissão
    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<StandardError> handleUnauthorizedOperation(
            UnauthorizedOperationException ex,
            HttpServletRequest request
    ) {
        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // 400 - argumentos inválidos / regra simples
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> handleBadRequest(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    //  400 - validação @Valid (se você habilitar)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String firstError = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .orElse("Dados inválidos.");

        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                firstError,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    //   401 - credenciais inválidas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Email ou senha inválidos.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    //  403 - bloqueio do Spring Security (role/endpoint)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "Você não tem permissão para acessar este recurso.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    //  500 - fallback real
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        // opcional: logar o stacktrace pra debug
        ex.printStackTrace();

        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}