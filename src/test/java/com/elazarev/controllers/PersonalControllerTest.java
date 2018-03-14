package com.elazarev.controllers;

import com.elazarev.Paths;
import com.elazarev.domain.Question;
import com.elazarev.domain.User;
import com.elazarev.service.QuestionService;
import com.elazarev.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for PersonalController class.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 12.03.18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonalControllerTest {
    /**
     * Mock mvc fro making requests.
     */
    @Autowired
    private MockMvc mockMvc;
    /**
     * Mock for question service.
     */
    @MockBean
    private QuestionService questionService;
    /**
     * Mock for user service.
     */
    @MockBean
    private UserService userService;

    /**
     * MyFeedPaged method test.
     * @throws Exception if error occur.
     */
    @Test
    @WithMockUser
    public void myFeedPaged() throws Exception {
        User author = new User();
        author.setId(2L);
        author.setLogin("admin");

        Question q = new Question();
        q.setId(1L);
        q.setAuthor(author);
        q.setCreateDate(LocalDateTime.now());

        Page<Question> page = new PageImpl<>(Arrays.asList(q));
        when(questionService.getMyFeedPage(eq(Optional.ofNullable(null)), any(Principal.class))).thenReturn(page);

        mockMvc.perform(get(Paths.PATH_MY_FEED))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("paginator"))
                .andExpect(model().attribute("paginator", is(page)));
    }

    /**
     * Profile method test.
     * @throws Exception if error occur.
     */
    @Test
    @WithMockUser
    public void profile() throws Exception {
        when(userService.getUser(any(Principal.class))).thenReturn(new User());
        mockMvc.perform(get(Paths.PATH_MY_PROFILE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"));
    }

    /**
     * Save profile method test.
     * @throws Exception if error occur.
     */
    @Test
    @WithMockUser
    public void saveProfile() throws Exception {
        User user = new User();
        user.setFirstName("first name");

        mockMvc.perform(post(Paths.PATH_MY_PROFILE)
                .with(csrf())
                .param("firstName", "first name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Paths.PATH_QUESTIONS_ALL));

        verify(userService).saveOnlyChangedFields(eq(user), any(Principal.class));
    }

}