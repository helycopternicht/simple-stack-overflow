package com.elazarev.controllers;

import com.elazarev.domain.Question;
import com.elazarev.domain.User;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.service.QuestionService;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 20.02.18
 */
@Controller
@RequestMapping(path = "/my")
public class PersonalController {

    private QuestionService questionService;

    @Autowired
    public PersonalController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/feed")
    public String myFeedPaged(@RequestParam Optional<Integer> page, Model model, Principal principal) {
        model.addAttribute("paginator", questionService.getMyFeedPage(page, principal));
        return "/question/questions";
    }

    @GetMapping("/profile")
    public String profile(Principal p) {
        // todo: implement profile
        return null;
    }
}
