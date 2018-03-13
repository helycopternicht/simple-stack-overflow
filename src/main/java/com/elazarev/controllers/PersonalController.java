package com.elazarev.controllers;

import com.elazarev.Paths;
import com.elazarev.domain.User;
import com.elazarev.service.QuestionService;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller for personal pages. Only for authorized users.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 20.02.18
 */
@Controller
public class PersonalController {
    /**
     * Question service.
     */
    private QuestionService questionService;
    /**
     * User service.
     */
    private UserService userService;

    /**
     * Constructor with all dependencies.
     * @param questionService question service.
     * @param userService user service.
     */
    @Autowired
    public PersonalController(QuestionService questionService, UserService userService) {
        this.questionService = questionService;
        this.userService = userService;
    }

    /**
     * Shows user feed. All questions witch related with users tags.
     * @param page page number.
     * @param model model.
     * @param principal current principal.
     * @return view name.
     */
    @GetMapping(Paths.PATH_MY_FEED)
    public String myFeedPaged(@RequestParam Optional<Integer> page, Model model, Principal principal) {
        model.addAttribute("paginator", questionService.getMyFeedPage(page, principal));
        return "/question/questions";
    }

    /**
     * User profile page.
     * @param p current user.
     * @param model model.
     * @return view name.
     */
    @GetMapping(Paths.PATH_MY_PROFILE)
    public String profile(Principal p, Model model) {
        model.addAttribute("user", userService.getUser(p));
        return "user/profile";
    }

    /**
     * Saving user profile changes.
     * @param form form with changes.
     * @param principal current user.
     * @return redirects on all question view.
     */
    @PostMapping(Paths.PATH_MY_PROFILE)
    public String saveProfile(@ModelAttribute User form, Principal principal) {
        // todo: some validating
        userService.saveOnlyChangedFields(form, principal);
        return "redirect:" + Paths.PATH_QUESTIONS_ALL;
    }
}
