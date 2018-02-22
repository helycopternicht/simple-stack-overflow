package com.elazarev.repository;

import com.elazarev.domain.Question;
import com.elazarev.domain.Tag;
import com.elazarev.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


import java.util.Collection;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Repository
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
    Page<Question> findByTagsIn(Collection<Tag> tags, Pageable pageable);

    Page<Question> findByTitleStartsWith(String s, Pageable pageable);

    Collection<Question> findAllByAuthorEquals(User u);
}
