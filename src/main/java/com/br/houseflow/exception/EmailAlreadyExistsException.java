package com.br.houseflow.exception;

public class EmailAlreadyExistsException extends RuntimeException {

  public EmailAlreadyExistsException() {
    super("Email já cadastrado");
  }
}

