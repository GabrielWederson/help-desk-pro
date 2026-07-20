package io.github.gabrielwederson.help_desk_pro.service;

import io.github.gabrielwederson.help_desk_pro.dto.security.RegisterRequest;
import io.github.gabrielwederson.help_desk_pro.dto.security.SignInRequestDTO;
import io.github.gabrielwederson.help_desk_pro.dto.security.TokenDTO;
import io.github.gabrielwederson.help_desk_pro.exceptions.InvalidDataException;
import io.github.gabrielwederson.help_desk_pro.exceptions.InvalidRefreshTokenException;
import io.github.gabrielwederson.help_desk_pro.model.User;
import io.github.gabrielwederson.help_desk_pro.repository.UserRepository;
import io.github.gabrielwederson.help_desk_pro.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService service;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void registerSuccessfully() {

        RegisterRequest request = new RegisterRequest(
                "gabriel@gmail.com",
                "Gabriel",
                "123456"
        );

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(request.email());
        savedUser.setName(request.name());
        savedUser.setPassword("encodedPassword");
        savedUser.setRoles(List.of("USER"));

        TokenDTO token = new TokenDTO();
        token.setUsername(savedUser.getEmail());
        token.setAccessToken("access-token");
        token.setRefreshToken("refresh-token");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenProvider.createAccessToken(savedUser.getEmail(), savedUser.getRoles()))
                .thenReturn(token);

        TokenDTO response = service.register(request);

        assertNotNull(response);
        assertEquals(token, response);

        verify(userRepository).existsByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(any(User.class));
        verify(tokenProvider).createAccessToken(savedUser.getEmail(), savedUser.getRoles());
    }

    @Test
    void registerShouldThrowExceptionWhenEmailAlreadyExists() {

        RegisterRequest request = new RegisterRequest(
                "gabriel@gmail.com",
                "Gabriel",
                "123456"
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.register(request)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository).existsByEmail(request.email());
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
        verify(tokenProvider, never()).createAccessToken(anyString(), anyList());
    }

    @Test
    void signinSuccessfully() {

        SignInRequestDTO request = new SignInRequestDTO(
                "gabriel@gmail.com",
                "123456"
        );

        User user = new User();
        user.setEmail(request.email());
        user.setPassword("encoded");
        user.setRoles(List.of("USER"));

        TokenDTO token = new TokenDTO();
        token.setUsername(user.getEmail());
        token.setAccessToken("access-token");
        token.setRefreshToken("refresh-token");

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        when(tokenProvider.createAccessToken(user.getEmail(), user.getRoles()))
                .thenReturn(token);

        TokenDTO response = service.signin(request);

        assertNotNull(response);
        assertEquals(token, response);

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(userRepository).findByEmail(request.email());

        verify(tokenProvider)
                .createAccessToken(user.getEmail(), user.getRoles());
    }

    @Test
    void signinShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {

        SignInRequestDTO request = new SignInRequestDTO(
                "gabriel@gmail.com",
                "123456"
        );

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.signin(request)
        );

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(userRepository).findByEmail(request.email());

        verify(tokenProvider, never())
                .createAccessToken(anyString(), anyList());
    }

    @Test
    void refreshTokenSuccessfully() {

        TokenDTO token = new TokenDTO();
        token.setUsername("gabriel@gmail.com");
        token.setAccessToken("access-token");
        token.setRefreshToken("refresh-token");

        User user = new User();
        user.setEmail("gabriel@gmail.com");
        user.setRoles(List.of("USER"));

        when(tokenProvider.refreshToken("Bearer refresh-token"))
                .thenReturn(token);

        when(userRepository.findByEmail("gabriel@gmail.com"))
                .thenReturn(Optional.of(user));

        TokenDTO response = service.refreshToken("Bearer refresh-token");

        assertNotNull(response);
        assertEquals(token, response);

        verify(tokenProvider).refreshToken("Bearer refresh-token");
        verify(userRepository).findByEmail("gabriel@gmail.com");
    }

    @Test
    void refreshTokenShouldThrowInvalidDataExceptionWhenTokenIsNull() {

        assertThrows(
                InvalidDataException.class,
                () -> service.refreshToken(null)
        );

        verifyNoInteractions(tokenProvider);
        verifyNoInteractions(userRepository);
    }

    @Test
    void refreshTokenShouldThrowInvalidDataExceptionWhenTokenIsBlank() {

        assertThrows(
                InvalidDataException.class,
                () -> service.refreshToken("")
        );

        verifyNoInteractions(tokenProvider);
        verifyNoInteractions(userRepository);
    }

    @Test
    void refreshTokenShouldThrowInvalidRefreshTokenExceptionWhenTokenProviderReturnsNull() {

        when(tokenProvider.refreshToken("Bearer refresh-token"))
                .thenReturn(null);

        assertThrows(
                InvalidRefreshTokenException.class,
                () -> service.refreshToken("Bearer refresh-token")
        );

        verify(tokenProvider).refreshToken("Bearer refresh-token");
        verifyNoInteractions(userRepository);
    }

    @Test
    void refreshTokenShouldThrowInvalidRefreshTokenExceptionWhenUserDoesNotExist() {

        TokenDTO token = new TokenDTO();
        token.setUsername("gabriel@gmail.com");

        when(tokenProvider.refreshToken("Bearer refresh-token"))
                .thenReturn(token);

        when(userRepository.findByEmail(token.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidRefreshTokenException.class,
                () -> service.refreshToken("Bearer refresh-token")
        );

        verify(userRepository).findByEmail(token.getUsername());
    }
}
