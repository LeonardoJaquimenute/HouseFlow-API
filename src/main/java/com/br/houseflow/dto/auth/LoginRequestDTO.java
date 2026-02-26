package com.br.houseflow.dto.auth;

public record LoginRequestDTO(
        String email,
        String password
) {}