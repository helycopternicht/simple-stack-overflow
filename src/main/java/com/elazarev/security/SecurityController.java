package com.elazarev.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 17.02.18
 */
@Controller
public class SecurityController {

    @RequestMapping("/login")
    public String login() {
        return "/auth/login";
    }

}
