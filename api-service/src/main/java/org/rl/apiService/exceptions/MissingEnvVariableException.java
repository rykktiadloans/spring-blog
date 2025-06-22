package org.rl.apiService.exceptions;

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
