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
import sun.security.acl.PrincipalImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 13.03.18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private UserService userService;

    @Test
    public void allQuestions() throws Exception {

        User user = new User();
        user.setLogin("admin");

        Question question = new Question();
        question.setAuthor(user);

        List<Question> questions = new ArrayList<>();
        questions.add(question);

        Page<Question> page = new PageImpl<>(questions);

        when(questionService.getQuestionPaged(eq(Optional.ofNullable(null)))).thenReturn(page);

        mockMvc.perform(get(Paths.PATH_QUESTIONS_ALL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("pager"))
                .andExpect(model().attributeExists("paginator"))
                .andExpect(model().attribute("paginator", page))
                .andExpect(view().name("/question/questions"));

        verify(questionService).getQuestionPaged(any());
    }

    @Test
    public void search() throws Exception {
        User user = new User();
        user.setLogin("admin");

        Question q1 = new Question();
        q1.setAuthor(user);

        Question q2 = new Question();
        q2.setAuthor(user);

        List<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);

        Page<Question> page = new PageImpl<>(questions);

        when(questionService.searchPage(eq("test"), eq(Optional.ofNullable(null)))).thenReturn(page);

        mockMvc.perform(get(Paths.PATH_QUESTIONS_SEARCH).param("searchText", "test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("pager"))
                .andExpect(model().attributeExists("paginator"))
                .andExpect(model().attribute("paginator", page))
                .andExpect(view().name("/question/questions"));

        verify(questionService).searchPage(anyString(), eq(Optional.empty()));
    }

    @Test
    @WithMockUser
    public void viewQuestion() throws Exception {
        User user = new User();
        user.setLogin("123");

        Question question = new Question();
        question.setAuthor(user);

        when(questionService.getById(999L)).thenReturn(question);
        when(userService.isUsersQuestion(eq(question), any(Principal.class))).thenReturn(Boolean.TRUE);

        mockMvc.perform(get(Paths.PATH_QUESTIONS_SHOW.replace("{id}", "999")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("question"))
                .andExpect(model().attribute("question", question))
                .andExpect(model().attribute("addSolutionButton", Boolean.TRUE))
                .andExpect(view().name("/question/details"));
    }

    @Test
    public void showAddForm() throws Exception {
        mockMvc.perform(get(Paths.PATH_QUESTIONS_ADD))
                .andExpect(status().isOk())
                .andExpect(view().name("/question/new"));
    }

    @Test
    @WithMockUser
    public void createQuestion() throws Exception {

        when(questionService.createQuestion(eq("title"), eq("desc"), eq("tag"), any(Principal.class)))
                .thenReturn(1000L);

        mockMvc.perform(post(Paths.PATH_QUESTIONS_ADD).with(csrf())
                .param("title", "title")
                .param("description", "desc")
                .param("tags", "tag").principal(new PrincipalImpl("user")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Paths.PATH_QUESTIONS_SHOW.replace("{id}", "1000")));

        verify(questionService).createQuestion(eq("title"), eq("desc"), eq("tag"), any(Principal.class));
    }

    @Test
    @WithMockUser
    public void addAnswer() throws Exception {
        String question_id = "100";
        mockMvc.perform(post(Paths.PATH_QUESTIONS_ANSWER_ADD)
                .param("text", "text")
                .param("question_id", question_id)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Paths.PATH_QUESTIONS_SHOW.replace("{id}", question_id)));

        verify(questionService).createAnswer(eq(100L), eq("text"), any(Principal.class));
    }

    @Test
    public void subscribe() throws Exception {
    }

    @Test
    public void like() throws Exception {
    }

    @Test
    public void solution() throws Exception {
    }

}