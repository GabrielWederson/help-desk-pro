package io.github.gabrielwederson.help_desk_pro.service;

import io.github.gabrielwederson.help_desk_pro.dto.security.RegisterRequest;
import io.github.gabrielwederson.help_desk_pro.dto.security.SignInRequestDTO;
import io.github.gabrielwederson.help_desk_pro.dto.security.TokenDTO;
import io.github.gabrielwederson.help_desk_pro.exceptions.InvalidDataException;
import io.github.gabrielwederson.help_desk_pro.exceptions.InvalidRefreshTokenException;
import io.github.gabrielwederson.help_desk_pro.exceptions.UserNotFoundException;
import io.github.gabrielwederson.help_desk_pro.model.User;
import io.github.gabrielwederson.help_desk_pro.repository.UserRepository;
import io.github.gabrielwederson.help_desk_pro.security.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public TokenDTO register(RegisterRequest request) {
        logger.info("Registering new user");

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(List.of("USER"));

        User savedUser = userRepository.save(user);

        var token = tokenProvider.createAccessToken(
                savedUser.getEmail(),
                savedUser.getRoles()
        );

        logger.info("User registered and authenticated");
        return token;
    }

    public TokenDTO signin(SignInRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found" ));

        var token = tokenProvider.createAccessToken(
                user.getEmail(),
                user.getRoles()
        );

        return token;
    }

    public TokenDTO refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new InvalidDataException("Refresh token cannot be null or empty");
        }

        try {

            TokenDTO token = tokenProvider.refreshToken(refreshToken);

            if (token == null) {
                throw new InvalidRefreshTokenException("Failed to refresh token");
            }

            var user = userRepository.findByEmail(token.getUsername())
                    .orElseThrow(() ->
                            new UserNotFoundException("User not found"));

            validateUserStatus(user);

            return token;

        } catch (Exception e) {
            throw new InvalidRefreshTokenException(
                    "Invalid or expired refresh token"
            );
        }
    }

    private void validateUserStatus(io.github.gabrielwederson.help_desk_pro.model.User user) {
        if (!user.isEnabled()) {
            throw new RuntimeException("User account is disabled");
        }

        if (!user.isAccountNonLocked()) {
            throw new RuntimeException("User account is locked");
        }

        if (!user.isAccountNonExpired()) {
            throw new RuntimeException("User account is expired");
        }

        if (!user.isCredentialsNonExpired()) {
            throw new RuntimeException("User credentials are expired");
        }
    }
}
