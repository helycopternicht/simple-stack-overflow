package com.elazarev.controllers;

import com.elazarev.domain.Question;
import com.elazarev.domain.User;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.service.QuestionService;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("")
    public String index(Model model, @RequestParam Optional<Integer> page) {

        Page<User> pager = userService.findAllPaged(page.orElse(1));

        Map<String, String> urls = new HashMap<>();
        urls.put("prevUrl", "/users/" + "?page=" +(page.orElse(1) - 1));
        urls.put("nextUrl", "/users/" + "?page=" +(page.orElse(1) - 1));

        model.addAttribute("page", pager);
        model.addAttribute("urls", urls);

        return "user/users";
    }

    @GetMapping("/{name}")
    public String details(Model model, @PathVariable String name) {

        Optional<User> user = Optional.ofNullable(userService.findUserByLogin(name));
        user.orElseThrow(() -> new ResourceNotFoundException("User with name " + name + "not found"));

        model.addAttribute("user", user.get());
        model.addAttribute("questionsWithUserAnswer", questionService.findAllWhereAuthorIs(user.get()));
        return "user/details";
    }

}
