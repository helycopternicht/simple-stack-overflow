package com.elazarev.controllers;

import com.elazarev.domain.Question;
import com.elazarev.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 15.02.18
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService service;

    @GetMapping("/questions")
    public String allQuestions(@RequestParam Optional<Integer> page, Model model) {

        model.addAttribute("questions", service.getAll());
        return "questions";
    }

    @GetMapping("/questions/{id}")
    public String viewQuestion(@PathVariable Long id, Model model) throws Exception {
        Optional<Question> question = service.getById(id);
        if (!question.isPresent()) {
            throw new Exception("Resource not found");
        }

        model.addAttribute(question.get());
        return "viewQuestion";
    }

}
