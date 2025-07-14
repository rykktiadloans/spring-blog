package org.rl.apiService.handlers;

import org.rl.shared.exceptions.MissingEntityException;
import org.rl.shared.exceptions.StorageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * The centralized exception handler
 */
@ControllerAdvice
public class ApiExceptionHandler {

    /**
     * Return HTTP code 404 Not Found in case {@link MissingEntityException} is thrown
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND
            , reason = "Entity was not found")
    @ExceptionHandler({
            MissingEntityException.class
    })
    public void handleMissingEntityException() {
    }

    /**
     * Return HTTP code 400 Bad Request in case {@link StorageException} is thrown
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST
            , reason = "Storage failure")
    @ExceptionHandler({
            StorageException.class
    })
    public void handleStorageException() {
    }

}
