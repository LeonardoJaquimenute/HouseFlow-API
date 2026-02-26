package com.br.houseflow.exception;

public class UnauthorizedOperationException extends RuntimeException {

    public UnauthorizedOperationException() {
        super("Você não tem permissão para realizar esta operação");
    }
}
