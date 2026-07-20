package io.github.gabrielwederson.help_desk_pro.exceptions;

public class WrongStatusException extends RuntimeException {
  public WrongStatusException(String message) {
    super(message);
  }
}
