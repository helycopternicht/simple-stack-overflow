package com.elazarev.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 01.03.18
 */
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ModelAndView notFound(Exception ex) {
        ModelAndView result = new ModelAndView("errors/error");
        result.addObject("message", ex.getMessage());
        return result;
    }

}
