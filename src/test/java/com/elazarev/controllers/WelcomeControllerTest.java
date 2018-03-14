package com.elazarev.controllers;

import com.elazarev.Paths;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for WelcomeController class.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.03.18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WelcomeControllerTest {
    /**
     * MVC mock.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Welcome method test.
     * @throws Exception if error occur.
     */
    @Test
    public void welcome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Paths.PATH_QUESTIONS_ALL));
    }

}