package org.rl.apiService.handlers;

import org.rl.shared.exceptions.MissingEntityException;
import org.rl.shared.exceptions.StorageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND
            , reason = "Entity was not found")
    @ExceptionHandler({
            MissingEntityException.class
    })
    public void handleMissingEntityException() {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST
            , reason = "Storage failure")
    @ExceptionHandler({
            StorageException.class
    })
    public void handleStorageException() {
    }

}
