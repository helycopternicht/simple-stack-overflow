package com.elazarev.service;

import com.elazarev.domain.Tag;
import com.elazarev.domain.User;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
    private UserService userService;

    @Autowired
    public TagsService(TagRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
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

    public Page<Tag> getTagPage(Optional<Integer> page) throws ResourceNotFoundException {
        Pageable pageRequest = PageRequest.of(page.orElse(1) - 1, MAX_TAG_SIZE_PER_PAGE);
        Page<Tag> currentPage = repo.findAll(pageRequest);
        if (currentPage.getNumberOfElements() == 0) {
            throw new ResourceNotFoundException("Page not found");
        }
        return currentPage;
    }

    public Tag getTagByName(String name) throws ResourceNotFoundException {
        Optional<Tag> tag = repo.findByName(name);
        return tag.orElseThrow(() -> new ResourceNotFoundException("Tag with name " + name + " not found"));
    }

    public boolean subscribe(Principal p, String tagName) {
        Tag tag = getTagByName(tagName);
        User user = userService.getUser(p);

        if (user.subscribedToTag(tag)) {
            return false;
        }

        user.getTags().add(tag);
        userService.save(user);
        return true;
    }
}
