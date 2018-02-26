package com.elazarev.service;

import com.elazarev.domain.Answer;
import com.elazarev.repository.AnswerRepository;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 25.02.18
 */

public class AnswerServiceTest {

    @Test
    public void saveMethodTest() {

        AnswerRepository repo = mock(AnswerRepository.class);
        Answer answer = mock(Answer.class);

        AnswerService service = new AnswerService(repo);
        service.save(answer);

        verify(repo).save(answer);
    }

    @Test
    public void findByIdTest() {
        AnswerRepository repo = mock(AnswerRepository.class);
        Answer answer = mock(Answer.class);
        Optional<Answer> o = Optional.of(answer);

        when(repo.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        when(repo.findById(1L)).thenReturn(o);

        AnswerService service = new AnswerService(repo);
        assertEquals(o, service.findById(1L));
        assertFalse(service.findById(2l).isPresent());
        assertFalse(service.findById(100l).isPresent());

        verify(repo, times(3)).findById(anyLong());
    }

}
