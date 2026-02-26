package com.br.houseflow.controller;

import com.br.houseflow.dto.user.CreateChildDTO;
import com.br.houseflow.dto.user.UserResponseDTO;
import com.br.houseflow.security.CustomUserDetails;
import com.br.houseflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('PARENT')")
    @PostMapping("/my-children")
    public ResponseEntity<UserResponseDTO> createChild(
            @Valid @RequestBody CreateChildDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createChildForParent(userDetails.user(), dto));
    }

    @PreAuthorize("hasRole('PARENT')")
    @GetMapping("/my-children")
    public ResponseEntity<List<UserResponseDTO>> myChildren(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.findChildrenOf(userDetails.user()));
    }

    // ✅ Ajuste 1: proteger /me (evita null userDetails)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.toResponseDTO(userDetails.user()));
    }
}