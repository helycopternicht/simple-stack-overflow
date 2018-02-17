package com.elazarev.repository;

import com.elazarev.domain.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {

    Tag findByName(String name);
}
