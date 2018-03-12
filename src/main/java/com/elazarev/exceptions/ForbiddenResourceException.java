package com.elazarev.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Throws if requested resource is forbidden for current user.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 28.02.18
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenResourceException extends RuntimeException {
    /**
     * Constructor with message.
     * @param message massage for user.
     */
    public ForbiddenResourceException(String message) {
        super(message);
    }
}
