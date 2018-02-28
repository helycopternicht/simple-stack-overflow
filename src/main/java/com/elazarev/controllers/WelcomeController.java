package com.elazarev.controllers;

import com.elazarev.Paths;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 20.02.18
 */
@Controller
public class WelcomeController {

    @RequestMapping(path = "/")
    public String welcome() {
        return "redirect:" + Paths.PATH_QUESTIONS_ALL;
    }

}
