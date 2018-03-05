package com.elazarev.service;

import com.elazarev.domain.Tag;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.repository.TagRepository;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test for class TagsService.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 27.02.18
 */
public class TagsServiceTest {
    /**
     * Test for createTags() method;
     */
    @Test
    public void createTags() {

        Tag tag1 = new Tag("1");
        Tag tag2 = new Tag("2");
        Tag tag3 = new Tag("3");
        Tag tag4 = new Tag("4");
        Tag tag5 = new Tag("5");

        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);
        tags.add(tag4);
        tags.add(tag5);

        List<Tag> tagsToSave = new ArrayList<>(Arrays.asList(tag4, tag5));

        TagRepository repository = mock(TagRepository.class);
        when(repository.findAllByNameIn(Arrays.asList("1", "2", "3", "4", "5"))).thenReturn(new ArrayList<>(Arrays.asList(tag1, tag2, tag3)));
        when(repository.saveAll(eq(tagsToSave))).thenReturn(new ArrayList<>(Arrays.asList(tag4, tag5)));

        TagsService service = new TagsService(repository);
        Iterable<Tag> it = service.createTags("1", "2", "3", "4", "5");
        List<Tag> list = new ArrayList<>();
        for (Tag tag : it) {
            list.add(tag);
        }

        assertThat(list.size(), is(5));
        assertThat(list.containsAll(tags), is(true));
    }

    /**
     * Test for getTagByName() method.
     */
    @Test(expected = ResourceNotFoundException.class)
    public void getTagByName() {

        Tag tag1 = new Tag("1");
        Tag tag2 = new Tag("2");
        Tag tag3 = new Tag("3");
        Tag tag4 = new Tag("4");
        Tag tag5 = new Tag("5");

        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);
        tags.add(tag4);
        tags.add(tag5);

        TagRepository repository = mock(TagRepository.class);
        when(repository.findByName(anyString())).thenReturn(Optional.ofNullable(null));
        for (Tag tag : tags) {
            when(repository.findByName(tag.getName())).thenReturn(Optional.of(tag));
        }

        TagsService service = new TagsService(repository);

        assertThat(service.getTagByName("1"), is(tag1));
        assertThat(service.getTagByName("2"), is(tag2));
        assertThat(service.getTagByName("3"), is(tag3));
        assertThat(service.getTagByName("4"), is(tag4));
        assertThat(service.getTagByName("5"), is(tag5));

        service.getTagByName("nonexistent tag"); // should throw exception
    }

    /**
     * Test for getTagPage() method.
     */
    @Test(expected = ResourceNotFoundException.class)
    public void getTagPage() {

        List<Tag> tags = new ArrayList<>(50);
        for (int i = 0; i < 50; i++) {
            tags.add(new Tag(String.valueOf(i)));
        }

        PageRequest req1 = PageRequest.of(0, TagsService.MAX_TAG_SIZE_PER_PAGE);
        PageRequest req2 = PageRequest.of(1, TagsService.MAX_TAG_SIZE_PER_PAGE);
        PageRequest req3 = PageRequest.of(2, TagsService.MAX_TAG_SIZE_PER_PAGE);

        Page<Tag> page1 = new PageImpl<>(tags, req1, TagsService.MAX_TAG_SIZE_PER_PAGE);
        Page<Tag> page2 = new PageImpl<>(tags, req2, TagsService.MAX_TAG_SIZE_PER_PAGE);
        Page<Tag> emptyPage = new PageImpl<>(Arrays.asList());

        TagRepository repository = mock(TagRepository.class);
        when(repository.findAll(eq(req1))).thenReturn(page1);
        when(repository.findAll(eq(req2))).thenReturn(page2);
        when(repository.findAll(eq(req3))).thenReturn(emptyPage);

        TagsService service = new TagsService(repository);

        Page<Tag> result1 = service.getTagPage(Optional.of(1));
        assertThat(result1.hasContent(), is(true));
        assertThat(result1.getNumber(), is(0));

        Page<Tag> result2 = service.getTagPage(Optional.of(2));
        assertThat(result2.hasContent(), is(true));
        assertThat(result2.getNumber(), is(1));

        Page<Tag> withoutPageNumber = service.getTagPage(Optional.ofNullable(null));
        assertThat(withoutPageNumber.hasContent(), is(true));
        assertThat(withoutPageNumber.getNumber(), is(0));

        verify(repository, times(3)).findAll(any(Pageable.class));

        service.getTagPage(Optional.of(3)); // should throw exception
    }

}
