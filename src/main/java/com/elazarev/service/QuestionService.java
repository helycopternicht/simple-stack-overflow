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

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 15.02.18
 */
@Service
public class QuestionService {

    private QuestionRepository questionRepository;

    private TagsService tagsService;

    private UserService userService;

    private AnswerService answerService;

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

    public static final int MAX_QUESTIONS_PER_PAGE = 10;

    public Question getById(Long id) throws ResourceNotFoundException {
        Optional<Question> question = questionRepository.findById(id);
        return question.orElseThrow(() -> new ResourceNotFoundException("Question with " + id + " not found."));
    }

    public Page<Question> getMyFeedPage(Optional<Integer> page, Principal p) throws ResourceNotFoundException {
        User user = userService.getUser(p);
        Page<Question> currentPage = questionRepository.findByTagsIn(user.getTags(),
                PageRequest.of(page.orElse(1) - 1,
                        MAX_QUESTIONS_PER_PAGE,
                        Sort.Direction.DESC,
                        "createDate"));

        if (currentPage.getNumberOfElements() == 0) {
            throw new ResourceNotFoundException("Page not found");
        }
        return currentPage;
    }

    public Page<Question> getQuestionPaged(Optional<Integer> page) throws ResourceNotFoundException {
        Page<Question> dataPage = questionRepository.findAll(
                PageRequest.of(
                        page.orElse(1) - 1,
                        MAX_QUESTIONS_PER_PAGE,
                        Sort.Direction.DESC,
                        "createDate"));

        if (dataPage.getNumberOfElements() == 0) {
            throw new ResourceNotFoundException("Have no page " + page.orElse(1));
        }
        return dataPage;
    }

    public Page<Question> search(String condition, Optional<Integer> page) throws ResourceNotFoundException {
        Pageable p = PageRequest.of(page.orElse(1) - 1, MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC, "createDate");
        Page<Question> result = questionRepository.findByTitleStartsWith(condition, p);
        if (result.getNumberOfElements() == 0) {
            throw new ResourceNotFoundException("Page not found");
        }
        return result;
    }

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

    public boolean markAsSolution (Principal p, Long answerId) {
        // todo: maybe it should be in answer service...
        User user = userService.getUser(p);
        Answer answer = answerService.findById(answerId);
        User questionAuthor = answer.getQuestion().getAuthor();

        if (!user.equals(questionAuthor)) {
            throw new NotAcceptableResource("Mark as solution may do only owner of question");
        }

        answer.setSolution(true);
        answerService.save(answer);
        return true;
    }

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

    private long saveWithTags(Question q, String tagList) {
        Iterable<Tag> it = tagsService.saveTags(tagList.split(","));
        it.forEach(tag -> q.getTags().add(tag));
        return questionRepository.save(q).getId();
    }

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
