package com.br.houseflow.service;

import com.br.houseflow.dto.auth.LoginDTO;
import com.br.houseflow.dto.auth.LoginResponseDTO;
import com.br.houseflow.dto.auth.RegisterDTO;
import com.br.houseflow.entity.user.Role;
import com.br.houseflow.entity.user.User;
import com.br.houseflow.repository.UserRepository;
import com.br.houseflow.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginDTO dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                )
        );

        String token = jwtService.generateToken(dto.email());

        return new LoginResponseDTO(token);
    }

    public LoginResponseDTO register(RegisterDTO dto) {

        if (dto.email() == null || dto.email().isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório.");
        }

        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.PARENT);

        userRepository.save(user);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                )
        );

        String token = jwtService.generateToken(dto.email());

        return new LoginResponseDTO(token);
    }
}