package com.elazarev.security;

import com.elazarev.domain.User;
import com.elazarev.security.excepttions.UserAlreadyExistsException;
import com.elazarev.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public String login() {
        return "/auth/login";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerUserForm", new RegisterUserForm());
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String tryRegisterUser(@Valid RegisterUserForm registerUserForm, Errors error, Model model) {

        if (error.hasErrors()) {
            model.addAttribute("registerUserForm", registerUserForm);
            return "/auth/registration";
        }

        User newUser = User.constructUser(registerUserForm, encoder);
        try {
            newUser = userService.createUser(newUser);
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("registerUserForm", registerUserForm);
            model.addAttribute("incorrectLogin", true);
            return "/auth/registration";
        }

        return "redirect:/my/feed";
    }

}
