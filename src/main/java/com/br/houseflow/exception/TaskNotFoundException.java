package com.br.houseflow.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException() {
        super("Tarefa não encontrada");
    }
}

