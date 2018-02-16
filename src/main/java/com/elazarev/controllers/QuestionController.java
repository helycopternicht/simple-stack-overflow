package com.elazarev.controllers;

import com.elazarev.domain.Question;
import com.elazarev.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 15.02.18
 */
@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService service;

    @GetMapping(path = {"/page/{page}", ""})
    public String allQuestions(@PathVariable Optional<Integer> page, Model model) {

        Page<Question> data;
        if (page.isPresent()) {
            data = service.getQuestionPaged(page.get());
        } else {
            data = service.getQuestionPaged(1);
        }

        if (page.isPresent() && page.get() > data.getTotalPages()) {
            return "forward:/error";
        }

//        int currentPage = data.getNumber() + 1;
//        int begin = Math.max(1, currentPage - 2);
//        int end = Math.min(begin + 2, data.getTotalPages());
//        List<Integer> pageIndexes = new ArrayList<>();
//        for (int i = begin; i <= end ; i++) {
//            pageIndexes.add(i);
//        }

        model.addAttribute("paginator", data);
//        model.addAttribute("pageIndexes", pageIndexes);
//        model.addAttribute("currentIndex", currentPage);

        return "questions";
    }

    @GetMapping("/{id}")
    public String viewQuestion(@PathVariable Long id, Model model) throws Exception {
        Optional<Question> question = service.getById(id);
        if (!question.isPresent()) {
            throw new Exception("Resource not found");
        }

        model.addAttribute("question", question.get());
        return "viewQuestion";
    }

}
