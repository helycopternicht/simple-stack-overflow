package com.elazarev.service;

import com.elazarev.domain.Answer;
import com.elazarev.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 21.02.18
 */
@Service
public class AnswerService {

    private AnswerRepository answerRepo;

    @Autowired
    public AnswerService(AnswerRepository answerRepo) {
        this.answerRepo = answerRepo;
    }

    public Optional<Answer> findById(Long id) {
        return answerRepo.findById(id);
    }

    public void save(Answer a) {
        answerRepo.save(a);
    }
}
