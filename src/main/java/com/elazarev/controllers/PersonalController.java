package com.elazarev.controllers;

import com.elazarev.domain.User;
import com.elazarev.service.QuestionService;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 20.02.18
 */
@Controller
@RequestMapping(path = "/my")
public class PersonalController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping("/feed")
    public String myFeed(Model model, Principal principal) {
        return myFeedPaged(1, model, principal);
    }

    @GetMapping("/feed/{page}")
    public String myFeedPaged(@PathVariable int page, Model model, Principal principal) {
        User user = userService.findUserByLogin(principal.getName());
        model.addAttribute("paginator", questionService.getMyFeedPaged(page, user));
        return "/question/questions";
    }

    @GetMapping("/profile")
    public String profile(Principal principal) {
        return null;
    }

}
