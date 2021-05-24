package org.cloudburstmc.protocol.java.exception;

public class ProfileException extends Exception {
    public ProfileException(String message) {
        super(message);
    }

    public ProfileException(Throwable throwable) {
        super(throwable);
    }

    public ProfileException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
