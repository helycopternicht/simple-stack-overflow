package com.elazarev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 20.02.18
 */
@Controller
@RequestMapping(path = "/my")
public class FeedController {

    @GetMapping("/feed")
    public String showMyFeed(Model model) {

        return "/question/questions";
    }

    @GetMapping("/feed-without-answers")
    public String showMyFeedWithoutAnswers(Model model) {

        return "/question/questions";
    }

}
