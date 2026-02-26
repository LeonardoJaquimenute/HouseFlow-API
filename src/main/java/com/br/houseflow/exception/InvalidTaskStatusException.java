package com.br.houseflow.exception;

public class InvalidTaskStatusException extends RuntimeException {

    public InvalidTaskStatusException() {
        super("Status de tarefa inválido");
    }
}
