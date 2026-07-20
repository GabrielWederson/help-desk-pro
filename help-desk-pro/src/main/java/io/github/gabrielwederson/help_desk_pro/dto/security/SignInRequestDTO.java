package io.github.gabrielwederson.help_desk_pro.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignInRequestDTO(@NotNull @Email String email, @NotNull String password) {
}
