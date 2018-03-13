package com.elazarev.controllers;

import com.elazarev.Paths;
import com.elazarev.domain.Tag;
import com.elazarev.service.TagsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for TagController.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 12.03.18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerTest {
    /**
     * Mock mvc for making requests.
     */
    @Autowired
    private MockMvc mockMvc;
    /**
     * Tag service mock.
     */
    @MockBean
    private TagsService tagsService;

    /**
     * Index method test.
     * @throws Exception if error occur.
     */
    @Test
    public void index() throws Exception {

        List<Tag> tags = new ArrayList<>(3);
        tags.add(new Tag("java"));
        tags.add(new Tag("sql"));
        tags.add(new Tag("maven"));

        Page<Tag> page = new PageImpl<>(tags,
                PageRequest.of(0, TagsService.MAX_TAG_SIZE_PER_PAGE),
                TagsService.MAX_TAG_SIZE_PER_PAGE);

        when(tagsService.getTagPage(eq(Optional.ofNullable(null)))).thenReturn(page);

        mockMvc.perform(get(Paths.PATH_TAGS_ALL))
                .andExpect(status().isOk())
                .andExpect(view().name("tag/tags"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attribute("page", is(page)));
    }

    /**
     * Details method test.
     * @throws Exception if error occur.
     */
    @Test
    public void details() throws Exception {
        Tag java = new Tag("java");
        when(tagsService.getTagByName("java")).thenReturn(java);

        mockMvc.perform(get(Paths.PATH_TAGS_SHOW.replace("{name}", "java")))
                .andExpect(status().isOk())
                .andExpect(view().name("tag/details"))
                .andExpect(model().attributeExists("tag"))
                .andExpect(model().attribute("tag", is(java)));

    }

    /**
     * Subscribe method test.
     * @throws Exception if error occur.
     */
    @Test
    @WithMockUser
    public void subscribe() throws Exception {
        String java = "java";

        mockMvc.perform(get(Paths.PATH_TAGS_SUBSCRIBE.replace("{name}", java)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Paths.PATH_TAGS_SHOW.replace("{name}", java)));
    }
}