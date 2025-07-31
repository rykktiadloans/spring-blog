package org.rl.frontendService.controllerAdvice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * A global exception handler
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * If a resource cannot be found (read: we're trying to access an unknown page), redirect to the frontend
     * @param request Request object
     * @return Frontend in index.html
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNotFoundException(WebRequest request) {
        return "forward:/index.html";
    }
}
