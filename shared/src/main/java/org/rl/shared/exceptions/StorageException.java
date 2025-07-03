package org.rl.shared.exceptions;

public class StorageException extends RuntimeException {

    public StorageException() {}
    public StorageException(String message) {
        super(message);
    }
    public StorageException(Throwable e) {
        super(e);
    }

    public StorageException(String message, Throwable e) {
        super(message, e);
    }
}
