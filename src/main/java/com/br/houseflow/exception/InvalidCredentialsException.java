package com.br.houseflow.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Email ou senha inválidos");
    }
}

