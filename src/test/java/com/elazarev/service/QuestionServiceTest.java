package com.elazarev.service;

import com.elazarev.domain.Answer;
import com.elazarev.domain.Question;
import com.elazarev.domain.Tag;
import com.elazarev.domain.User;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.repository.QuestionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 27.02.18
 */
public class QuestionServiceTest {

    private QuestionRepository questionRepository;

    private TagsService tagsService;

    private UserService userService;

    private AnswerService answerService;

    private QuestionService questionService;

    @Before
    public void setUp() throws Exception {
        questionRepository = mock(QuestionRepository.class);
        tagsService = mock(TagsService.class);
        userService = mock(UserService.class);
        answerService = mock(AnswerService.class);
        questionService = new QuestionService(questionRepository, tagsService, userService, answerService);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getById() {

        Question q1 = new Question();

        when(questionRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        when(questionRepository.findById(999L)).thenReturn(Optional.of(q1));

        assertThat(questionService.getById(999L), is(q1));
        questionService.getById(888L); // should throw exception
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getMyFeedPage() throws Exception {

        User user = new User();
        when(userService.getUser(Mockito.any(Principal.class))).thenReturn(user);

        List<Question> list = new ArrayList<>(11);
        for (int i = 0; i < 11; i++) {
            list.add(new Question());
        }

        PageRequest req1 = PageRequest.of(0, QuestionService.MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC,"createDate");
        PageRequest req2 = PageRequest.of(1, QuestionService.MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC,"createDate");
        PageRequest req3 = PageRequest.of(2, QuestionService.MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC,"createDate");

        Page<Question> page1 = new PageImpl<>(list.subList(0, QuestionService.MAX_QUESTIONS_PER_PAGE), req1, list.size());
        Page<Question> page2 = new PageImpl<>(list.subList(QuestionService.MAX_QUESTIONS_PER_PAGE, list.size()), req1, list.size());
        Page<Question> empty = Page.empty();

        when(questionRepository.findByTagsIn(anyCollection(), Mockito.eq(req1))).thenReturn(page1);
        when(questionRepository.findByTagsIn(anyCollection(), Mockito.eq(req2))).thenReturn(page2);
        when(questionRepository.findByTagsIn(anyCollection(), Mockito.eq(req3))).thenReturn(empty);

        Page<Question> res1 = questionService.getMyFeedPage(Optional.of(1), new PrincipalImpl("user"));
        Page<Question> res2 = questionService.getMyFeedPage(Optional.of(2), new PrincipalImpl("user"));
        Page<Question> res3 = questionService.getMyFeedPage(Optional.ofNullable(null), new PrincipalImpl("user"));

        assertThat(res1, is(page1));
        assertThat(res2, is(page2));
        assertThat(res3, is(page1));

        questionService.getMyFeedPage(Optional.of(3), new PrincipalImpl("user")); // should throw exception
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getQuestionPaged() throws Exception {
        List<Question> list = new ArrayList<>(11);
        for (int i = 0; i < 11; i++) {
            list.add(new Question());
        }

        PageRequest req1 = PageRequest.of(0, QuestionService.MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC,"createDate");
        PageRequest req2 = PageRequest.of(1, QuestionService.MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC,"createDate");
        PageRequest req3 = PageRequest.of(2, QuestionService.MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC,"createDate");

        Page<Question> page1 = new PageImpl<>(list.subList(0, QuestionService.MAX_QUESTIONS_PER_PAGE), req1, list.size());
        Page<Question> page2 = new PageImpl<>(list.subList(QuestionService.MAX_QUESTIONS_PER_PAGE, list.size()), req1, list.size());
        Page<Question> empty = Page.empty();

        when(questionRepository.findAll(eq(req1))).thenReturn(page1);
        when(questionRepository.findAll(eq(req2))).thenReturn(page2);
        when(questionRepository.findAll(eq(req3))).thenReturn(empty);

        Page<Question> res1 = questionService.getQuestionPaged(Optional.of(1));
        Page<Question> res2 = questionService.getQuestionPaged(Optional.of(2));
        Page<Question> res3 = questionService.getQuestionPaged(Optional.ofNullable(null));

        assertThat(res1, is(page1));
        assertThat(res2, is(page2));
        assertThat(res3, is(page1));

        questionService.getQuestionPaged(Optional.of(3)); // should throw exception
    }

    @Test
    public void searchPage() throws Exception {
        List<Question> list = new ArrayList<>(11);
        for (int i = 0; i < 11; i++) {
            list.add(new Question());
        }

        PageRequest req1 = PageRequest.of(0, QuestionService.MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC,"createDate");
        PageRequest req2 = PageRequest.of(1, QuestionService.MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC,"createDate");
        PageRequest req3 = PageRequest.of(2, QuestionService.MAX_QUESTIONS_PER_PAGE, Sort.Direction.DESC,"createDate");

        Page<Question> page1 = new PageImpl<>(list.subList(0, QuestionService.MAX_QUESTIONS_PER_PAGE), req1, list.size());
        Page<Question> page2 = new PageImpl<>(list.subList(QuestionService.MAX_QUESTIONS_PER_PAGE, list.size()), req1, list.size());
        Page<Question> empty = Page.empty();

        when(questionRepository.findByTitleStartsWith(anyString(), eq(req1))).thenReturn(page1);
        when(questionRepository.findByTitleStartsWith(anyString(), eq(req2))).thenReturn(page2);
        when(questionRepository.findByTitleStartsWith(anyString(), eq(req3))).thenReturn(empty);

        Page<Question> res1 = questionService.searchPage("test", Optional.of(1));
        Page<Question> res2 = questionService.searchPage("test", Optional.of(2));
        Page<Question> res3 = questionService.searchPage("test", Optional.ofNullable(null));
        Page<Question> res4 = questionService.searchPage("test", Optional.of(3));

        assertThat(res1, is(page1));
        assertThat(res2, is(page2));
        assertThat(res3, is(page1));
        assertThat(res4.hasContent(), is(false));
    }

    @Test
    public void subscribe() throws Exception {
        User user = new User();
        Question q1 = new Question();

        when(userService.getUser(Mockito.any(Principal.class))).thenReturn(user);
        when(questionRepository.findById(1900L)).thenReturn(Optional.of(q1));

        assertThat(questionService.subscribe(1900L, new PrincipalImpl("user")), is(true));
        assertThat(questionService.subscribe(1900L, new PrincipalImpl("user")), is(false));
        verify(questionRepository).save(q1);
    }

    @Test
    public void likeAnswer() throws Exception {
        User user = new User();
        when(userService.getUser(Mockito.any(Principal.class))).thenReturn(user);

        Answer answer = new Answer();
        when(answerService.findById(9876L)).thenReturn(answer);

        assertThat(questionService.likeAnswer(new PrincipalImpl("user"), 9876L), is(true));
        assertThat(questionService.likeAnswer(new PrincipalImpl("user"), 9876L), is(false));
        assertThat(answer.getLiked().contains(user), is(true));
    }

    @Test
    public void markAsSolution() throws Exception {
        User questionOwner = new User();
        questionOwner.setLogin("owner");
        questionOwner.setId(999L);

        User user = new User();
        user.setLogin("someohteruser");
        user.setId(8888L);

        Principal owner = new PrincipalImpl("owner");
        Principal someOtherUser = new PrincipalImpl("user");

        Question question = new Question();
        question.setAuthor(questionOwner);
        Answer answer = new Answer();
        answer.setQuestion(question);

        questionOwner.getQuestions().add(question);

        when(userService.getUser(owner)).thenReturn(questionOwner);
        when(userService.getUser(someOtherUser)).thenReturn(user);
        when(answerService.findById(anyLong())).thenReturn(answer);

        assertThat(questionService.markAsSolution(owner, 48731L), is(true));
        assertThat(questionService.markAsSolution(someOtherUser, 48731L), is(false));

        verify(answerService).save(answer);
    }

    @Test
    public void createQuestion() throws Exception {
        Tag java = new Tag("java");
        User user = new User();
        Question q = new Question();
        q.setAuthor(user);
        q.setId(999L);
        q.setTitle("Question?");
        q.setClosed(false);
        q.setCreateDate(LocalDateTime.now());
        q.setDescription("description");
        q.getSubscribers().add(user);

        when(userService.getUser(Mockito.any(Principal.class))).thenReturn(user);
        when(tagsService.createTags(anyString())).thenReturn(Arrays.asList(java));
        when(questionRepository.save(Mockito.any(Question.class))).thenReturn(q);

        long id = questionService.createQuestion("Question?", "description", "java", new PrincipalImpl("user"));

        assertThat(id, is(999L));
    }
}
