package com.elazarev.repository;

import com.elazarev.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for answers table.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
