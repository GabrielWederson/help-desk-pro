package io.github.gabrielwederson.help_desk_pro.controller;

import io.github.gabrielwederson.help_desk_pro.controller.docs.AuthControllerDocs;
import io.github.gabrielwederson.help_desk_pro.dto.security.RegisterRequest;
import io.github.gabrielwederson.help_desk_pro.dto.security.SignInRequestDTO;
import io.github.gabrielwederson.help_desk_pro.dto.security.TokenDTO;
import io.github.gabrielwederson.help_desk_pro.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController implements AuthControllerDocs {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Override
    public ResponseEntity<TokenDTO> register(@Valid @RequestBody RegisterRequest request) {
        TokenDTO token = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/signin")
    @Override
    public ResponseEntity<TokenDTO> signin(@Valid @RequestBody SignInRequestDTO request) {
        TokenDTO token = authService.signin(request);
        return ResponseEntity.ok(token);
    }

    @PutMapping("/refresh")
    @Override
    public ResponseEntity<TokenDTO> refreshToken(
            @RequestHeader("Authorization") String refreshToken) {

        TokenDTO token = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(token);
    }
}
