package org.softuni.esports.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "You are not allowed here.")
public class ForbiddenError extends RuntimeException {
}
