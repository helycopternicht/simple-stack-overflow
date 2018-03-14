package com.elazarev.controllers;

import com.elazarev.Paths;
import com.elazarev.domain.User;
import com.elazarev.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for UserController class.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.03.18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    /**
     * MVC mock.
     */
    @Autowired
    private MockMvc mockMvc;
    /**
     * User service mock.
     */
    @MockBean
    private UserService userService;

    /**
     * Index method test.
     * @throws Exception if error occur.
     */
    @Test
    public void index() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());

        Page<User> page = new PageImpl<>(users);
        when(userService.getPage(eq(Optional.empty()))).thenReturn(page);

        mockMvc.perform(get(Paths.PATH_USERS_ALL))
                .andExpect(status().isOk())
                .andExpect(model().attribute("page", page))
                .andExpect(model().attributeExists("urls"))
                .andExpect(view().name("user/users"));

        verify(userService).getPage(any(Optional.class));
    }

    /**
     * Details method test.
     * @throws Exception if error occur.
     */
    @Test
    public void details() throws Exception {

        User user = new User();
        user.setLogin("admin");

        when(userService.findByLogin(eq("admin"))).thenReturn(user);

        mockMvc.perform(get(Paths.PATH_USERS_SHOW.replace("{name}", user.getLogin())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", is(user)))
                .andExpect(view().name("user/details"));

        verify(userService).findByLogin(eq(user.getLogin()));
    }
}