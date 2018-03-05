package com.elazarev.service;

import com.elazarev.domain.Answer;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.repository.AnswerRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 25.02.18
 */

public class AnswerServiceTest {

    @Test(expected = ResourceNotFoundException.class)
    public void findById() {

        AnswerRepository repo = mock(AnswerRepository.class);

        Answer firstAnswer = new Answer();
        firstAnswer.setId(9990L);
        firstAnswer.setText("Test answer");

        when(repo.findById(9990L)).thenReturn(Optional.of(firstAnswer));

        AnswerService service = new AnswerService(repo);

        assertSame(firstAnswer, service.findById(9990L));
        service.findById(2L); // should throw exception
    }

    @Test
    public void save() {

        AnswerRepository repo = mock(AnswerRepository.class);

        Answer answer = new Answer();
        answer.setText("Test answer");

        Answer res = new Answer();
        res.setText(answer.getText());
        res.setId(9998L);

        when(repo.save(answer)).thenReturn(res);

        AnswerService service = new AnswerService(repo);

        assertThat(service.save(answer), is(9998L));
    }



}
