package com.elazarev.controllers;

import com.elazarev.domain.Answer;
import com.elazarev.domain.Question;
import com.elazarev.domain.User;
import com.elazarev.repository.AnswerRepository;
import com.elazarev.service.QuestionService;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private AnswerRepository anserverRepo;

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

    @GetMapping("/show/{id}")
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
        return "/question/newQuestion";
    }

    @PostMapping("/add")
    public String createQuestion(@RequestParam("title") String title,
                                 @RequestParam("tags") String stringTags,
                                 @RequestParam("description") String description) {

        // todo: add class to avoid dublicate code
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(((User)auth.getPrincipal()).getId()).get();

        Question q = new Question();
        q.setAuthor(user);
        q.setTitle(title);
        q.setClosed(false);
        q.setDescription(description);
        q.getSubscribers().add(user);
        long id = questionService.saveWithTags(q, stringTags);
        return "redirect:/questions/show/" + id;
    }

    @PostMapping("/answer")
    public String addAnswer(@RequestParam("text") String text, @RequestParam("question_id") Long id, HttpServletResponse resp) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(((User)auth.getPrincipal()).getId()).get();

        Optional<Question> question = questionService.getById(id);
        if (!question.isPresent() || text.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "redirect:/error";
        }

        Answer a = new Answer();
        a.setAuthor(user);
        a.setQuestion(question.get());
        a.setSolution(false);
        a.setText(text);

        anserverRepo.save(a);
        return "redirect:/questions/show/" + id;
    }

}
