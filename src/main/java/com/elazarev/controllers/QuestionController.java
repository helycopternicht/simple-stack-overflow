package com.elazarev.controllers;

import com.elazarev.domain.Question;
import com.elazarev.domain.User;
import com.elazarev.service.QuestionService;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 15.02.18
 */
@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping(path = {"/page/{page}", ""})
    public String allQuestions(@PathVariable Optional<Integer> page, Model model) {

        Page<Question> data;
        if (page.isPresent()) {
            data = questionService.getQuestionPaged(page.get());
        } else {
            data = questionService.getQuestionPaged(1);
        }

        if (page.isPresent() && page.get() > data.getTotalPages()) {
            return "forward:/error";
        }

        model.addAttribute("paginator", data);

        return "/question/questions";
    }

    @GetMapping("/{id}")
    public String viewQuestion(@PathVariable Long id, Model model) throws Exception {
        Optional<Question> question = questionService.getById(id);
        if (!question.isPresent()) {
            throw new Exception("Resource not found");
        }

        model.addAttribute("question", question.get());
        return "/question/viewQuestion";
    }

    @GetMapping("/add")
    public String showAddForm() {
        return "newQuestion";
    }

    @PostMapping("/add")
    public String createQuestion(@RequestParam("title") String title,
                                 @RequestParam("tags") String stringTags,
                                 @RequestParam("description") String description) {

        Optional<User> user = userService.findById(1L);

        Question q = new Question();
        q.setAuthor(user.get());
        q.setTitle(title);
        q.setClosed(false);
        q.setDescription(description);
        q.getSubscribers().add(user.get());
        long id = questionService.saveWithTags(q, stringTags);
        return "redirect:/question/questions/" + id;
    }

}
