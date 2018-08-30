package org.softuni.esports.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Access Denied!")
public class UnauthorizedError extends RuntimeException {
}
