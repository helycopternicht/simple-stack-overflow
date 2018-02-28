package com.elazarev.service;

import com.elazarev.domain.Tag;
import com.elazarev.repository.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 27.02.18
 */
public class TagsServiceTest {

//    private TagRepository tagRepository;
//
//    private TagsService tagService;
//
//    @Before
//    public void setUp() throws Exception {
//        tagRepository = mock(TagRepository.class);
//        tagService = new TagsService(tagRepository);
//    }
//
//    @Test
//    public void saveTags() throws Exception {
//
//        Tag java = new Tag();
//        java.setId(1L);
//        java.setName("java");
//
//        Tag sql = new Tag();
//        sql.setId(2L);
//        sql.setName("sql");
//
//        Tag javascript = new Tag();
//        javascript.setId(3L);
//        javascript.setName("javascript");
//
//        List<String> tags = Arrays.asList("java", "sql", "javascript");
//        when(tagRepository.findAllByNameIn(tags)).thenReturn(new ArrayList<>(Arrays.asList(java, sql)));
//        when(tagRepository.saveAll(anyIterable())).thenReturn(Arrays.asList(javascript));
//
//        Iterable<Tag> it = tagService.saveTags(new String []{"java", "sql", "javascript"});
//        List<Tag> resultList = new ArrayList<>(3);
//        it.forEach( e ->  resultList.add(e));
//
//        assertThat(resultList.size(), is(3));
//        assertThat(resultList.containsAll(Arrays.asList(java, sql, javascript)), is(true));
//
//        verify(tagRepository).findAllByNameIn(anyCollection());
//        verify(tagRepository).saveAll(anyIterable());
//    }
//
//    @Test
//    public void findAll() throws Exception {
//        Page<Tag> pageOne = mock(Page.class);
//        when(pageOne.getNumber()).thenReturn(0);
//
//        Page<Tag> pageTwo = mock(Page.class);
//        when(pageTwo.getNumber()).thenReturn(1);
//
//        PageRequest requestOne = PageRequest.of(0, TagsService.MAX_TAG_SIZE_PER_PAGE);
//        PageRequest requestTwo = PageRequest.of(1, TagsService.MAX_TAG_SIZE_PER_PAGE);
//
//        when(tagRepository.findAll(eq(requestOne))).thenReturn(pageOne);
//        when(tagRepository.findAll(eq(requestTwo))).thenReturn(pageTwo);
//
//        assertEquals(tagService.findAll(1), pageOne);
//        assertEquals(tagService.findAll(2), pageTwo);
//
//        verify(tagRepository).findAll(eq(requestOne));
//        verify(tagRepository).findAll(eq(requestTwo));
//    }
//
//    @Test
//    public void findByName() throws Exception {
//
//        Tag tag = new Tag("java");
//        when(tagRepository.findByName("java")).thenReturn(tag);
//
//        assertNull(tagService.findByName("someTagName"));
//        assertThat(tagService.findByName("java"), is(tag));
//
//        verify(tagRepository, times(2)).findByName(anyString());
//    }
//
//    @Test
//    public void findById() throws Exception {
//
//        Tag tag = new Tag("sometag");
//
//        when(tagRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
//        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
//
//        assertTrue(tagService.findById(1L).isPresent());
//        assertFalse(tagService.findById(2L).isPresent());
//
//        verify(tagRepository, times(2)).findById(anyLong());
//    }

}
