package com.br.houseflow.service;

import com.br.houseflow.dto.user.CreateChildDTO;
import com.br.houseflow.dto.user.UserResponseDTO;
import com.br.houseflow.entity.user.Role;
import com.br.houseflow.entity.user.User;
import com.br.houseflow.exception.UnauthorizedOperationException;
import com.br.houseflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public UserResponseDTO createChildForParent(User parent, CreateChildDTO dto) {

        if (parent.getRole() != Role.PARENT) {
            throw new UnauthorizedOperationException();
        }

        if (dto.email() == null || dto.email().isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório.");
        }

        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        User child = new User();
        child.setName(dto.name());
        child.setEmail(dto.email());
        child.setPassword(passwordEncoder.encode(dto.password()));
        child.setRole(Role.CHILD);
        child.setParent(parent);

        return toResponseDTO(userRepository.save(child));
    }

    public List<UserResponseDTO> findChildrenOf(User parent) {

        if (parent.getRole() != Role.PARENT) {
            throw new UnauthorizedOperationException();
        }

        return userRepository.findByParentId(parent.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
}