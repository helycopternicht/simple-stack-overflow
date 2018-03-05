package com.elazarev.service;

import com.elazarev.domain.Answer;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class for answer business logic.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 21.02.18
 */
@Service
public class AnswerService {
    /**
     * Answer data access object.
     */
    private AnswerRepository answerRepo;

    /**
     * Constructor with repository.
     * @param answerRepo answer repository.
     */
    @Autowired
    public AnswerService(AnswerRepository answerRepo) {
        this.answerRepo = answerRepo;
    }

    /**
     * Returns answer by id.
     * @param id id of answer.
     * @return answer.
     * @throws ResourceNotFoundException if answer with represented id is not found.
     */
    public Answer findById(Long id) throws ResourceNotFoundException {
        return answerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + id + " not found"));
    }

    /**
     * Persist answer in repository.
     * @param a answer to save.
     * @return id of created answer.
     */
    public Long save(Answer a) {
        Answer created =  answerRepo.save(a);
        return created.getId();
    }
}
