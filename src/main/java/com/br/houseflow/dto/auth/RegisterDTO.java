package com.br.houseflow.dto.auth;

public record RegisterDTO(
        String name,
        String email,
        String password
) {}