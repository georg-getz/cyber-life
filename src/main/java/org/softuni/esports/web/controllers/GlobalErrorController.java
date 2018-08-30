package org.softuni.esports.web.controllers;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalErrorController extends BaseController {
    private static final String DEFAULT_ERROR_MESSAGE = "Something went wrong.";

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView getException(RuntimeException e){
        return this.view("error", "error",
                e.getClass().isAnnotationPresent(ResponseStatus.class)
                ? e.getClass().getAnnotation(ResponseStatus.class).reason()
                : DEFAULT_ERROR_MESSAGE);
    }

}
