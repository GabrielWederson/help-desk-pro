package io.github.gabrielwederson.help_desk_pro.exceptions;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String description) {
}
