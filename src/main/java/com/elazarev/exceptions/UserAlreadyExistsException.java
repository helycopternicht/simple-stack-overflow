package com.elazarev.exceptions;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 20.02.18
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
