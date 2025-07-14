package org.rl.shared.exceptions;

/**
 * Exception to throw when a database entity could not be found
 */
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
