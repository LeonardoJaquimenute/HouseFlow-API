package com.br.houseflow.controller;

import com.br.houseflow.dto.auth.LoginDTO;
import com.br.houseflow.dto.auth.LoginResponseDTO;
import com.br.houseflow.dto.auth.RegisterDTO;
import com.br.houseflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO dto) {
        return authService.login(dto);
    }

    @PostMapping("/register")
    public LoginResponseDTO register(@RequestBody RegisterDTO dto) {
        return authService.register(dto);
    }
}