package com.elazarev.controllers;

import com.elazarev.Paths;
import com.elazarev.domain.Question;
import com.elazarev.service.QuestionService;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

/**
 * Controller for questions.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 15.02.18
 */
@Controller
public class QuestionController {
    /**
     * Question service.
     */
    private QuestionService questionService;
    /**
     * User service.
     */
    private UserService userService;

    /**
     * Controller with all dependencies.
     * @param questionService question service.
     * @param userService user service.
     */
    @Autowired
    public QuestionController(QuestionService questionService, UserService userService) {
        this.questionService = questionService;
        this.userService = userService;
    }

    /**
     * Shows all questions paged.
     * @param page page number.
     * @param model model.
     * @return view name.
     */
    @GetMapping(Paths.PATH_QUESTIONS_ALL)
    public String allQuestions(@RequestParam Optional<Integer> page, Model model) {

        Page<Question> data = questionService.getQuestionPaged(page);
        Map<String, String> pager = new HashMap<>();
        pager.put("prevUrl", Paths.PATH_QUESTIONS_ALL + "?page=" + (page.orElse(1) - 1));
        pager.put("nextUrl", Paths.PATH_QUESTIONS_ALL + "?page=" + (page.orElse(1) + 1));

        model.addAttribute("paginator", data);
        model.addAttribute("pager", pager);

        return "/question/questions";
    }

    /**
     * Search results page.
     * @param searchText search string.
     * @param page page number.
     * @param model model.
     * @return view name.
     */
    @GetMapping(Paths.PATH_QUESTIONS_SEARCH)
    public String search(@RequestParam String searchText,
                         @RequestParam Optional<Integer> page,
                         Model model) {

        Map<String, String> pager = new HashMap<>();
        pager.put("prevUrl", Paths.PATH_QUESTIONS_SEARCH + "?searchText=" + searchText + "&page=" +(page.orElse(1) - 1));
        pager.put("nextUrl", Paths.PATH_QUESTIONS_SEARCH + "?searchText=" + searchText + "&page=" +(page.orElse(1) + 1));

        model.addAttribute("paginator", questionService.searchPage(searchText, page));
        model.addAttribute("pager", pager);
        return "/question/questions";
    }

    /**
     * Question details page.
     * @param id question id.
     * @param model model.
     * @param principal current user if present.
     * @return view name.
     */
    @GetMapping(Paths.PATH_QUESTIONS_SHOW)
    public String viewQuestion(@PathVariable Long id, Model model, Principal principal) {
        Question question = questionService.getById(id);
        model.addAttribute("question", question);
        model.addAttribute("addSolutionButton", userService.isUsersQuestion(question, principal));
        return "/question/details";
    }

    /**
     * Show form for new question.
     * @return view name.
     */
    @GetMapping(Paths.PATH_QUESTIONS_ADD)
    public String showAddForm() {
        return "/question/new";
    }

    /**
     * Creates new question.
     * @param title question title.
     * @param stringTags question tags in string format.
     * @param description question description.
     * @param principal current user.
     * @return redirects to question details.
     */
    @PostMapping(Paths.PATH_QUESTIONS_ADD)
    public String createQuestion(@RequestParam("title") String title,
                                 @RequestParam("tags") String stringTags,
                                 @RequestParam("description") String description,
                                 Principal principal) {
        // todo: implement validation!!!
        long id = questionService.createQuestion(title, description, stringTags, principal);
        return "redirect:" + Paths.PATH_QUESTIONS_SHOW.replace("{id}", ""+id);
    }

    /**
     * Adds answer to question.
     * @param text answer text.
     * @param id question id.
     * @param principal current user.
     * @return redirects to question details.
     */
    @PostMapping(Paths.PATH_QUESTIONS_ANSWER_ADD)
    public String addAnswer(@RequestParam("text") String text, @RequestParam("question_id") Long id, Principal principal) {
        // todo: implement validation!!!
        questionService.createAnswer(id, text, principal);
        return "redirect:" + Paths.PATH_QUESTIONS_SHOW.replace("{id}", ""+id);
    }

    /**
     * Endpoint for user subscribe to question.
     * @param id question id.
     * @param principal current user.
     * @return redirects to question details.
     */
    @PostMapping(Paths.PATH_QUESTIONS_SUBSCRIBE)
    public String subscribe(@RequestParam("question_id") Long id, Principal principal) {
        // todo: remove quest id from params. It can be taken from answer model.
        questionService.subscribe(id, principal);
        return "redirect:" + Paths.PATH_QUESTIONS_SHOW.replace("{id}", ""+id);
    }

    /**
     * Endpoint for user like answer.
     * @param id answer id.
     * @param qId question id.
     * @param principal current user.
     * @return redirects to question details.
     */
    @PostMapping(Paths.PATH_QUESTIONS_ANSWER_LIKE)
    public String like(@RequestParam("answer_id") Long id, @RequestParam("question_id") Long qId, Principal principal) {
        questionService.likeAnswer(principal, id);
        return "redirect:" + Paths.PATH_QUESTIONS_SHOW.replace("{id}", ""+qId);
    }

    /**
     * Endpoint for user marks answer as solution.
     * @param id answer id.
     * @param qId question id.
     * @param principal current user.
     * @return redirects to question details.
     */
    @PostMapping(Paths.PATH_QUESTIONS_ANSWER_SOLUTION)
    public String solution(@RequestParam("answer_id") Long id, @RequestParam("question_id") Long qId, Principal principal) {
        questionService.markAsSolution(principal, id);
        return "redirect:" + Paths.PATH_QUESTIONS_SHOW.replace("{id}", ""+qId);
    }
}
