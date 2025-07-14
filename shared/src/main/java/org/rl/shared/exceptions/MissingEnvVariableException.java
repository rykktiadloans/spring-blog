package org.rl.shared.exceptions;

/**
 * Exception to throw when an environmental variable isn't set
 */
public class MissingEnvVariableException extends RuntimeException {
    public MissingEnvVariableException() {}
    public MissingEnvVariableException(String message) {
        super(message);
    }
    public MissingEnvVariableException(Throwable e) {
        super(e);
    }
    public MissingEnvVariableException(String message, Throwable e) {
        super(message, e);
    }
}
