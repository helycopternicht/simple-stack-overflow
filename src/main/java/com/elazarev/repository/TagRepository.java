package com.elazarev.repository;

import com.elazarev.domain.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Repository for tags table.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
    /**
     * Find tag by specified name.
     * @param name name of tag.
     * @return tag in optional wrapper.
     */
    Optional<Tag> findByName(String name);

    /**
     * Finds all tags whose names are in the specified collection.
     * @param names collections of tag names.
     * @return list of tags.
     */
    List<Tag> findAllByNameIn(Collection<String> names);
}
