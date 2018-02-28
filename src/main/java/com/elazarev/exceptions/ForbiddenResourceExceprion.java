package com.elazarev.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 28.02.18
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenResourceExceprion extends RuntimeException {
    public ForbiddenResourceExceprion(String message) {
        super(message);
    }
}
