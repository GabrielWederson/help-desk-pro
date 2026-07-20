package io.github.gabrielwederson.help_desk_pro.exceptions;

public class InvalidJWTAuthenticationException extends RuntimeException {
  public InvalidJWTAuthenticationException(String message) {
    super(message);
  }
}
