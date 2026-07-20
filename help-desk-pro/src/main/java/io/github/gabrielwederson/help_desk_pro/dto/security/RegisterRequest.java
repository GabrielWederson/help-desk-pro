package io.github.gabrielwederson.help_desk_pro.dto.security;

import jakarta.validation.constraints.*;

public record RegisterRequest(@NotBlank @Email String email, @NotBlank String name, @NotBlank String password) {
}
