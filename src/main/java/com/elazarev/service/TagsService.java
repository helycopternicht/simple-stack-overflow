package com.elazarev.service;

import com.elazarev.domain.Tag;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class for tags business logic.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 16.02.18
 */
@Service
public class TagsService {
    /**
     * Number of tags on one paged. Used for pagination on tags page.
     */
    public static final int MAX_TAG_SIZE_PER_PAGE = 30;

    /**
     * Data access object for tags.
     */
    private TagRepository tagRepository;

    /**
     * Constructor with repository.
     * @param repo tags data access object.
     */
    @Autowired
    public TagsService(TagRepository repo) {
        this.tagRepository = repo;
    }

    /**
     * Method creates nonexistent tags by name and find existing tags.
     * Put all in entire list and return.
     * @param tags tag names to create and find.
     * @return entire list of tags.
     */
    public Iterable<Tag> createTags(String... tags) {

        List<Tag> existTags = findExistingTagsByNames(tags);
        List<String> existingNames = getNamesList(existTags);

        List<Tag> nonExistent = Arrays.stream(tags)
                .filter(e -> !existingNames.contains(e))
                .map(e -> new Tag(e))
                .collect(Collectors.toList());

        if (nonExistent.size() > 0 ) {
            Iterable<Tag> saved = tagRepository.saveAll(nonExistent);
            saved.forEach(e -> existTags.add(e));
        }
        return existTags;
    }

    /**
     * Returns all tags from repo by names.
     * @param tagNames names of tags to find.
     * @return list of existing tags.
     */
    private List<Tag> findExistingTagsByNames(String[] tagNames) {
        return tagRepository.findAllByNameIn(Arrays.asList(tagNames));
    }

    /**
     * Returns names list of represented list of tags.
     * @param list tag list.
     * @return lists of tag names.
     */
    private List<String> getNamesList(List<Tag> list) {
        return list.stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
    }

    /**
     * Returns tags in paged format.
     * @param page number of page to return.
     * @return page of tags for page number.
     * @throws ResourceNotFoundException if returned page hasn't content.
     */
    public Page<Tag> getTagPage(Optional<Integer> page) throws ResourceNotFoundException {
        Pageable pageRequest = PageRequest.of(page.orElse(1) - 1, MAX_TAG_SIZE_PER_PAGE);
        Page<Tag> currentPage = tagRepository.findAll(pageRequest);
        if (!currentPage.hasContent()) {
            throw new ResourceNotFoundException("Page not found");
        }
        return currentPage;
    }

    /**
     * Returns tag by name if presented.
     * @param name name to find.
     * @return tag with represented name.
     * @throws ResourceNotFoundException if tag with such name if not found.
     */
    public Tag getTagByName(String name) throws ResourceNotFoundException {
        Optional<Tag> tag = tagRepository.findByName(name);
        return tag.orElseThrow(() -> new ResourceNotFoundException("Tag with name " + name + " not found"));
    }
}
