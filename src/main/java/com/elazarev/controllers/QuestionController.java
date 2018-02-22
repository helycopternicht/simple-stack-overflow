package com.elazarev.controllers;

import com.elazarev.domain.Answer;
import com.elazarev.domain.Question;
import com.elazarev.domain.User;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.service.AnswerService;
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
import java.security.Principal;
import java.time.LocalDateTime;
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
    private AnswerService answerService;

    @GetMapping(path = {"/page/{page}", ""})
    public String allQuestions(@PathVariable Optional<Integer> page, Model model) {

        Page<Question> data = questionService.getQuestionPaged(page.orElse(1));

        if (page.isPresent() && page.get() > data.getTotalPages()) {
            return "forward:/error";
        }

        Map<String, String> pager = new HashMap<>();
        pager.put("prevUrl", "/questions/page/" + (page.orElse(1) - 1));
        pager.put("nextUrl", "/questions/page/" + (page.orElse(1) + 1));

        model.addAttribute("paginator", data);
        model.addAttribute("pager", pager);

        return "/question/questions";
    }

    @GetMapping("/show/{id}")
    public String viewQuestion(@PathVariable Long id, Model model, Principal principal) throws Exception {
        Optional<Question> question = questionService.getById(id);
        if (!question.isPresent()) {
            throw new Exception("Resource not found");
        }


        if (principal != null) {
            User user = userService.findUserByLogin(principal.getName());
            if (user.equals(question.get().getAuthor())) {
                model.addAttribute("isYour", true);
            }
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
        q.setCreateDate(LocalDateTime.now());
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
        a.setCreateDate(LocalDateTime.now());

        answerService.save(a);
        return "redirect:/questions/show/" + id;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam("question_id") Long id, Principal principal) {
        User user = userService.findUserByLogin(principal.getName());
        boolean found = false;
        for (Question q : user.getSubscriptions()) {
            if (q.getId().equals(id)) {
                found = true;
            }
        }

        if (!found) {
            questionService.subscribe(user, id);
        }
        return "redirect:/questions/show/" + id;
    }

    @PostMapping("/answer/like")
    public String like(@RequestParam("answer_id") Long id,
                       Principal principal,
                       HttpServletResponse resp) {

        User user = userService.findUserByLogin(principal.getName());
        Optional<Answer> optionalAnswer = answerService.findById(id);
        if (!optionalAnswer.isPresent()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "redirect:/error";
        }

        Answer answer =  optionalAnswer.get();
        boolean found = false;
        for (User u : answer.getLiked()) {
            if (u.getId().equals(user.getId())) {
                found = true;
                break;
            }
        }

        if (!found) {
            answer.getLiked().add(user);
            answerService.save(answer);
        }

        return "redirect:/questions/show/" + answer.getQuestion().getId();
    }

    @PostMapping("/answer/solution")
    public String solution(@RequestParam("answer_id") Long id, HttpServletResponse resp, Principal principal) {

        User user = userService.findUserByLogin(principal.getName());

        Optional<Answer> answer = answerService.findById(id);
        if (!answer.isPresent()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "redirect:/error";
        }

        Question question = answer.get().getQuestion();
        if (!user.equals(question.getAuthor())) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return "redirect:/error";
        }

        answer.get().setSolution(true);
        answerService.save(answer.get());
        return "redirect:/questions/show/" + question.getId();
    }

    @GetMapping("/search")
    public String search(@RequestParam String searchText,
                         @RequestParam Optional<Integer> page,
                         Model model) throws ResourceNotFoundException {

        Page<Question> questions = questionService.search(searchText, page.orElse(1));
        if (questions.getSize() == 0) {
            throw new ResourceNotFoundException(); //todo: make with errorHandler
        }

        Map<String, String> pager = new HashMap<>();
        pager.put("prevUrl", "/questions/search?searchText=" + searchText + "&page=" +(page.orElse(1) - 1));
        pager.put("nextUrl", "/questions/search?searchText=" + searchText + "&page=" +(page.orElse(1) + 1));

        model.addAttribute("paginator", questions);
        model.addAttribute("pager", pager);
        return "/question/questions";
    }

}
