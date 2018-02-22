package com.elazarev.service;

import com.elazarev.domain.Question;
import com.elazarev.domain.Tag;
import com.elazarev.domain.User;
import com.elazarev.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 15.02.18
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagsService tagsService;

    public static final int MAX_QUESTIONS_PER_PAGE = 10;

    public Iterable<Question> getAll() {
        return questionRepository.findAll();
    }

    public Optional<Question> getById(Long id) {
        return questionRepository.findById(id);
    }

    public Page<Question> getMyFeedPaged(int page, User user) {
        return questionRepository.findByTagsIn(user.getTags(), PageRequest.of(page - 1, MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC, "createDate"));
    }

    public Page<Question> getQuestionPaged(int page) {
        return questionRepository.findAll(PageRequest.of(page - 1, MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC, "createDate"));
    }

    public long saveWithTags(Question q, String tagList) {
        Iterable<Tag> it = tagsService.saveTags(tagList.split(","));
        it.forEach(tag -> q.getTags().add(tag));
        return questionRepository.save(q).getId();
    }

    public boolean subscribe(User u, Long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);
        if (question.isPresent()) {
            Question q = question.get();
            q.getSubscribers().add(u);
            questionRepository.save(q);
            return true;
        }
        return false;
    }

    public Page<Question> search(String condition, int page) {
        Pageable p = PageRequest.of(page - 1, MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC, "createDate");
        return questionRepository.findByTitleStartsWith(condition, p);
    }
}
