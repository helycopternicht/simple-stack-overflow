package com.elazarev.service;

import com.elazarev.domain.Tag;
import com.elazarev.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 16.02.18
 */
@Service
public class TagsService {

    public static final int MAX_TAG_SIZE_PER_PAGE = 30;

    private TagRepository repo;

    @Autowired
    public TagsService(TagRepository repo) {
        this.repo = repo;
    }

    public Iterable<Tag> saveTags(String[] tags) {
        List<Tag> result = new ArrayList<>();
        Tag oldTag = null;
        for (String tagName : tags) {
            oldTag = repo.findByName(tagName);
            if (oldTag != null) {
                result.add(oldTag);
            } else {
                result.add(new Tag(tagName));
            }
        }
        return repo.saveAll(result);
    }

    public Page<Tag> findAll(int page) {
        Pageable pageRequest = PageRequest.of(page - 1, MAX_TAG_SIZE_PER_PAGE);
        return repo.findAll(pageRequest);
    }

    public Tag findByName(String name) {
        return repo.findByName(name);
    }

    public Optional<Tag> findById(long id) {
        return repo.findById(id);
    }

}
