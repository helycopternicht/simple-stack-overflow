package com.elazarev.service;

import com.elazarev.domain.Answer;
import com.elazarev.domain.Question;
import com.elazarev.domain.Tag;
import com.elazarev.domain.User;
import com.elazarev.exceptions.NotAcceptableResource;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Class for questions business logic.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 15.02.18
 */
@Service
public class QuestionService {
    /**
     * Question data access object.
     */
    private QuestionRepository questionRepository;
    /**
     * Tag service.
     */
    private TagsService tagsService;
    /**
     * User service.
     */
    private UserService userService;
    /**
     * Answer service.
     */
    private AnswerService answerService;
    /**
     * Value of maximum question showing per page.
     */
    public static final int MAX_QUESTIONS_PER_PAGE = 10;

    /**
     * Constructor with all dependencies.
     * @param questionRepository question repository.
     * @param tagsService tags service.
     * @param userService user service.
     * @param answerService answer service.
     */
    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           TagsService tagsService,
                           UserService userService,
                           AnswerService answerService) {

        this.questionRepository = questionRepository;
        this.tagsService = tagsService;
        this.userService = userService;
        this.answerService = answerService;
    }

    /**
     * Returns question by id.
     * @param id id to find.
     * @return question.
     * @throws ResourceNotFoundException if question with that id is not found.
     */
    public Question getById(Long id) throws ResourceNotFoundException {
        Optional<Question> question = questionRepository.findById(id);
        return question.orElseThrow(() -> new ResourceNotFoundException("Question with " + id + " not found."));
    }

    /**
     * Return page of users feed (questions witch have tags same with users tags)
     * @param page number of page.
     * @param p current user.
     * @return page of questions
     * @throws ResourceNotFoundException if no questions in feed.
     */
    public Page<Question> getMyFeedPage(Optional<Integer> page, Principal p) throws ResourceNotFoundException {
        User user = userService.getUser(p);
        Page<Question> currentPage = questionRepository.findByTagsIn(user.getTags(),
                PageRequest.of(page.orElse(1) - 1,
                        MAX_QUESTIONS_PER_PAGE,
                        Sort.Direction.DESC,
                        "createDate"));

        if (!currentPage.hasContent()) {
            throw new ResourceNotFoundException("Page not found");
        }
        return currentPage;
    }

    /**
     * Returns page with all questions.
     * @param page number of page.
     * @return page of questions.
     * @throws ResourceNotFoundException if have no questions.
     */
    public Page<Question> getQuestionPaged(Optional<Integer> page) throws ResourceNotFoundException {
        Page<Question> dataPage = questionRepository.findAll(
                PageRequest.of(
                        page.orElse(1) - 1,
                        MAX_QUESTIONS_PER_PAGE,
                        Sort.Direction.DESC,
                        "createDate"));

        if (!dataPage.hasContent()) {
            throw new ResourceNotFoundException("Have no page " + page.orElse(1));
        }
        return dataPage;
    }

    /**
     * Returns page of search results.
     * @param condition key word to search.
     * @param page number of page.
     * @return page of questions (search results).
     */
    public Page<Question> searchPage(String condition, Optional<Integer> page) {
        Pageable p = PageRequest.of(page.orElse(1) - 1, MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC, "createDate");
        Page<Question> result = questionRepository.findByTitleStartsWith(condition, p);
        return result;
    }

    /**
     * Subscribe current user to question with specified id.
     * @param qId id of question.
     * @param p current user.
     * @return true if success or false else.
     */
    public boolean subscribe(Long qId, Principal p) {
        User user = userService.getUser(p);
        Question question = getById(qId);
        if (question.userSubscribed(user)) {
            return false;
        }

        question.getSubscribers().add(user);
        questionRepository.save(question);
        return true;
    }

    /**
     * Adds answer with specified id to liked for current user.
     * @param p current user.
     * @param answerId answer id.
     * @return true if success or false else.
     */
    public boolean likeAnswer(Principal p, Long answerId) {
        User user = userService.getUser(p);
        Answer answer = answerService.findById(answerId);

        if (answer.hasFan(user)) {
            return false;
        }

        answer.getLiked().add(user);
        answerService.save(answer);
        return true;
    }

    /**
     * Marks answer with specified id as solution.
     * @param p current user.
     * @param answerId answer id.
     * @return true if current user is ower of question and operation success.
     */
    public boolean markAsSolution (Principal p, Long answerId) {
        // todo: maybe it should be in answer service...
        User user = userService.getUser(p);
        Answer answer = answerService.findById(answerId);
        User questionAuthor = answer.getQuestion().getAuthor();

        if (!user.equals(questionAuthor)) {
            return false;
        }

        answer.setSolution(true);
        answerService.save(answer);
        return true;
    }

    /**
     * Creates and persist question in repository.
     * @param title question title.
     * @param desc question description.
     * @param tags question tags in string format.
     * @param p current user.
     * @return id of created question.
     */
    public Long createQuestion(String title, String desc, String tags, Principal p) {
        User user = userService.getUser(p);

        Question q = new Question();
        q.setAuthor(user);
        q.setTitle(title);
        q.setClosed(false);
        q.setCreateDate(LocalDateTime.now());
        q.setDescription(desc);
        q.getSubscribers().add(user);
        return saveWithTags(q, tags);
    }

    /**
     * Attach to specified question tags and persist question in repository.
     * @param q question.
     * @param tagList tags.
     * @return created question id.
     */
    private long saveWithTags(Question q, String tagList) {
        Iterable<Tag> it = tagsService.createTags(tagList.split(","));
        it.forEach(tag -> q.getTags().add(tag));
        return questionRepository.save(q).getId();
    }

    /**
     * Creates and persist answer in repository.
     * @param qId question id.
     * @param text text of answer.
     * @param p current user.
     * @return id of created answer.
     */
    public Long createAnswer(Long qId, String text, Principal p) {
        // todo: maybe it should be in answer service...
        Question question = getById(qId);
        User author = userService.getUser(p);

        Answer a = new Answer();
        a.setAuthor(author);
        a.setQuestion(question);
        a.setSolution(false);
        a.setText(text);
        a.setCreateDate(LocalDateTime.now());
        return answerService.save(a);
    }
}
