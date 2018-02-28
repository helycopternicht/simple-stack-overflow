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

    private QuestionService questionService;

    private UserService userService;

    @Autowired
    public QuestionController(QuestionService questionService, UserService userService) {
        this.questionService = questionService;
        this.userService = userService;
    }

    @GetMapping("")
    public String allQuestions(@RequestParam Optional<Integer> page, Model model) {

        Page<Question> data = questionService.getQuestionPaged(page);
        Map<String, String> pager = new HashMap<>();
        pager.put("prevUrl", "/questions?page=" + (page.orElse(1) - 1));
        pager.put("nextUrl", "/questions?page=" + (page.orElse(1) + 1));

        model.addAttribute("paginator", data);
        model.addAttribute("pager", pager);

        return "/question/questions";
    }

    @GetMapping("/search")
    public String search(@RequestParam String searchText,
                         @RequestParam Optional<Integer> page,
                         Model model) {



        Map<String, String> pager = new HashMap<>();
        pager.put("prevUrl", "/questions/search?searchText=" + searchText + "&page=" +(page.orElse(1) - 1));
        pager.put("nextUrl", "/questions/search?searchText=" + searchText + "&page=" +(page.orElse(1) + 1));

        model.addAttribute("paginator", questionService.search(searchText, page));
        model.addAttribute("pager", pager);
        return "/question/questions";
    }

    @GetMapping("/show/{id}")
    public String viewQuestion(@PathVariable Long id, Model model, Principal principal) throws Exception {
        Question question = questionService.getById(id);
        model.addAttribute("question", question);
        model.addAttribute("addSolutionButton", userService.isQuestionOfUser(question, principal));

        return "/question/viewQuestion";
    }

    @GetMapping("/add")
    public String showAddForm() {
        return "/question/newQuestion";
    }

    @PostMapping("/add")
    public String createQuestion(@RequestParam("title") String title,
                                 @RequestParam("tags") String stringTags,
                                 @RequestParam("description") String description,
                                 Principal principal) {
        // todo: implement validation!!!
        long id = questionService.createQuestion(title, description, stringTags, principal);
        return "redirect:/questions/show/" + id;
    }

    @PostMapping("/answer")
    public String addAnswer(@RequestParam("text") String text, @RequestParam("question_id") Long id, Principal principal) {
        // todo: implement validation!!!
        questionService.createAnswer(id, text, principal);
        return "redirect:/questions/show/" + id;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam("question_id") Long id, Principal principal) {
        questionService.subscribe(id, principal);
        return "redirect:/questions/show/" + id;
    }

    @PostMapping("/answer/like")
    public String like(@RequestParam("answer_id") Long id, @RequestParam("question_id") Long qId, Principal principal) {
        questionService.likeAnswer(principal, id);
        return "redirect:/questions/show/" + qId;
    }

    @PostMapping("/answer/solution")
    public String solution(@RequestParam("answer_id") Long id, @RequestParam("question_id") Long qId, Principal principal) {
        questionService.markAsSolution(principal, id);
        return "redirect:/questions/show/" + qId;
    }
}
