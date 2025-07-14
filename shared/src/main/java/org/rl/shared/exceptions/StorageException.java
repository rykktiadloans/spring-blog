package org.rl.shared.exceptions;

/**
 * Exception to throw in case of storage issues
 */
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
