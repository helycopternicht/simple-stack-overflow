package com.elazarev.service;

import com.elazarev.domain.Tag;
import com.elazarev.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 16.02.18
 */
@Service
public class TagsService {

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

}
