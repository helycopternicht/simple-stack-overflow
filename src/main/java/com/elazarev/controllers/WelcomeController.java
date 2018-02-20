package com.elazarev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 20.02.18
 */
@Controller
public class WelcomeController {

    @RequestMapping(path = "/")
    public String welcome() {
        return "forward:/questions";
    }
}
