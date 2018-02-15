package com.elazarev.service;

import com.elazarev.domain.Question;
import com.elazarev.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Iterable<Question> getAll() {
        return repo.findAll();
    }

    public Optional<Question> getById(Long id) {
        return repo.findById(id);
    }

}
