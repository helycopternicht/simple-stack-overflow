package com.elazarev.service;

import com.elazarev.domain.Tag;
import com.elazarev.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

        List<Tag> existTags = repo.findAllByNameIn(Arrays.asList(tags));
        List<String> existingNames = existTags.stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());

        List<Tag> nonExistent = Arrays.stream(tags)
                .filter(e -> !existingNames.contains(e))
                .map(e -> new Tag(e))
                .collect(Collectors.toList());

        if (nonExistent.size() > 0 ) {
            Iterable<Tag> saved = repo.saveAll(nonExistent);
            saved.forEach(e -> existTags.add(e));
        }
        return existTags;
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
