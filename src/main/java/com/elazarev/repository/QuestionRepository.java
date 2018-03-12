package com.elazarev.repository;

import com.elazarev.domain.Question;
import com.elazarev.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Repository for questions table.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Repository
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
    /**
     * Returns all questions related with specified collection of tags in
     * paged format.
     * @param tags tags to find.
     * @param pageable paging configuration.
     * @return page of questions.
     */
    Page<Question> findByTagsIn(Collection<Tag> tags, Pageable pageable);

    /**
     * Method returns page of questions where title starts with specified string s.
     * @param s string to search.
     * @param pageable paging configuration.
     * @return page of questions.
     */
    Page<Question> findByTitleStartsWith(String s, Pageable pageable);
}
