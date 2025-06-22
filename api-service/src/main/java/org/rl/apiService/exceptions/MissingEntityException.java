package org.rl.apiService.exceptions;

public class MissingEntityException extends RuntimeException {
    public MissingEntityException() {}
    public MissingEntityException(String message) {
        super(message);
    }
    public MissingEntityException(Throwable e) {
        super(e);
    }

    public MissingEntityException(String message, Throwable e) {
        super(message, e);
    }
}
