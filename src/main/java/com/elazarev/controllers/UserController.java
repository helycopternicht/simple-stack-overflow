package com.elazarev.controllers;

import com.elazarev.Paths;
import com.elazarev.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 21.02.18
 */
@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(Paths.PATH_USERS_ALL)
    public String index(Model model, @RequestParam Optional<Integer> page) {

        Map<String, String> urls = new HashMap<>();
        urls.put("prevUrl", "/users/" + "?page=" +(page.orElse(1) - 1));
        urls.put("nextUrl", "/users/" + "?page=" +(page.orElse(1) - 1));

        model.addAttribute("page", userService.getPage(page));
        model.addAttribute("urls", urls);

        return "user/users";
    }

    @GetMapping(Paths.PATH_USERS_SHOW)
    public String details(Model model, @PathVariable String name) {
        model.addAttribute("user", userService.findByLogin(name));
        return "user/details";
    }
}
