package com.br.houseflow.dto.user;


import com.br.houseflow.entity.user.Role;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        Role role
) {}
