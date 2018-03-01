package com.elazarev.security;

import com.elazarev.Paths;
import com.elazarev.domain.User;
import com.elazarev.exceptions.UserAlreadyExistsException;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.Errors;

import javax.validation.Valid;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 17.02.18
 */
@Controller
public class SecurityController {

    UserService userService;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(Paths.PATH_LOGIN)
    public String login() {
        return "/auth/login";
    }

    @GetMapping(Paths.PATH_REGISTER)
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerUserForm", new RegisterUserForm());
        return "/auth/registration";
    }

    @PostMapping(Paths.PATH_REGISTER)
    public String tryRegisterUser(@Valid RegisterUserForm registerUserForm, Errors error, Model model) {

        if (error.hasErrors()) {
            model.addAttribute("registerUserForm", registerUserForm);
            return "/auth/registration";
        }

        User newUser = registerUserForm.constructUser();
        try {
            userService.createUser(newUser);
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("registerUserForm", registerUserForm);
            model.addAttribute("incorrectLogin", true);
            return "/auth/registration";
        }

        return "redirect:" + Paths.PATH_QUESTIONS_ALL;
    }

}
