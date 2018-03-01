package com.elazarev.controllers;

import com.elazarev.Paths;
import com.elazarev.domain.Question;
import com.elazarev.domain.User;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.security.RegisterUserForm;
import com.elazarev.service.QuestionService;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 20.02.18
 */
@Controller
public class PersonalController {

    private QuestionService questionService;

    private UserService userService;

    @Autowired
    public PersonalController(QuestionService questionService, UserService userService) {
        this.questionService = questionService;
        this.userService = userService;
    }

    @GetMapping(Paths.PATH_MY_FEED)
    public String myFeedPaged(@RequestParam Optional<Integer> page, Model model, Principal principal) {
        model.addAttribute("paginator", questionService.getMyFeedPage(page, principal));
        return "/question/questions";
    }

    @GetMapping(Paths.PATH_MY_PROFILE)
    public String profile(Principal p, Model model) {
        model.addAttribute("user", userService.getUser(p));
        return "user/profile";
    }

    @PostMapping(Paths.PATH_MY_PROFILE)
    public String saveProfile(@ModelAttribute User form, Principal principal) {
        userService.saveOnlyChangedFields(form, principal);
        return "redirect:" + Paths.PATH_QUESTIONS_ALL;
    }
}
