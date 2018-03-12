package com.elazarev.exceptions;

/**
 * Throws when trying create user witch already exists.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 20.02.18
 */
public class UserAlreadyExistsException extends RuntimeException {
    /**
     * Constructor with message.
     * @param message massage for user.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
