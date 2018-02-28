package com.elazarev.service;

import com.elazarev.domain.Answer;
import com.elazarev.domain.Question;
import com.elazarev.exceptions.ResourceNotFoundException;
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

    public Answer findById(Long id) {
        return answerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + id + " not found"));
    }

    public Long save(Answer a) {
        Answer created =  answerRepo.save(a);
        return created.getId();
    }
}
