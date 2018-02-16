package com.elazarev.service;

import com.elazarev.domain.Question;
import com.elazarev.domain.User;
import com.elazarev.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private QuestionRepository repo;

    public static final int MAX_QUESTIONS_PER_PAGE = 10;

    public Iterable<Question> getAll() {
        return repo.findAll();
    }

    public Optional<Question> getById(Long id) {
        return repo.findById(id);
    }

    public Page<Question> getQuestionPaged(int page) {
        return repo.findAll(PageRequest.of(page - 1, MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC, "createDate"));
    }

}
