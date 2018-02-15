package com.elazarev.repository;

import com.elazarev.domain.Question;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Repository
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
}
