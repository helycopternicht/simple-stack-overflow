package com.elazarev.service;

import com.elazarev.domain.Role;
import com.elazarev.domain.User;
import com.elazarev.repository.RoleRepository;
import com.elazarev.repository.UserRepository;
import com.elazarev.security.excepttions.UserAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 26.02.18
 */
public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userService = new UserService(userRepository, roleRepository);
    }

    @Test
    public void findById() throws Exception {
        Optional<User> user = Optional.of(new User());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(null));
        when(userRepository.findById(1L)).thenReturn(user);

        assertEquals(user, userService.findById(1L));
        assertFalse(userService.findById(2L).isPresent());

        verify(userRepository, times(2)).findById(anyLong());
    }

    @Test
    public void findUserByLogin() throws Exception {

        User user = mock(User.class);

        when(userRepository.findUserByLogin(anyString())).thenReturn(null);
        when(userRepository.findUserByLogin("AnyName")).thenReturn(user);

        assertThat(user, is(userService.findUserByLogin("AnyName")));
        assertNull(userService.findUserByLogin("SomeOther"));

        verify(userRepository).findUserByLogin("AnyName");
        verify(userRepository).findUserByLogin("SomeOther");

    }

    @Test
    public void findAllPaged() throws Exception {

        Page<User> pageOne = mock(Page.class);
        when(pageOne.getNumber()).thenReturn(0);

        Page<User> pageTwo = mock(Page.class);
        when(pageTwo.getNumber()).thenReturn(1);


        PageRequest requestOne = PageRequest.of(0, UserService.MAX_USERS_PER_PAGE, Sort.Direction.DESC, "id");
        PageRequest requestTwo = PageRequest.of(1, UserService.MAX_USERS_PER_PAGE, Sort.Direction.DESC, "id");

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(null);
        when(userRepository.findAll(eq(requestOne))).thenReturn(pageOne);
        when(userRepository.findAll(eq(requestTwo))).thenReturn(pageTwo);

        assertEquals(userService.findAllPaged(1), pageOne);
        assertEquals(userService.findAllPaged(2), pageTwo);

        verify(userRepository).findAll(eq(requestOne));
        verify(userRepository).findAll(eq(requestTwo));
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createUser() throws Exception {

        Role user_role = mock(Role.class);
        when(roleRepository.findByName(RoleRepository.ROLE_NAME_USER)).thenReturn(user_role);

        User user = mock(User.class);
        when(user.getLogin()).thenReturn("User");

        User someUser = mock(User.class);
        when(someUser.getLogin()).thenReturn("someUser");
        when(someUser.getId()).thenReturn(100L);
        when(someUser.getRoles()).thenReturn(new HashSet<>());

        when(userRepository.save(someUser)).thenReturn(someUser);
        when(userRepository.findUserByLogin("User")).thenReturn(user);

        User created = userService.createUser(someUser);
        assertNotNull(created);
        assertThat(created.getId(), is(100L));
        assertThat(created.getLogin(), is("someUser"));
        assertTrue(created.getRoles().contains(user_role));

        verify(userRepository).findUserByLogin(anyString());
        verify(userRepository).save(any());
        verify(roleRepository).findByName(RoleRepository.ROLE_NAME_USER);

        // should throw exception
        userService.createUser(user);
    }

    @Test
    public void save() throws Exception {

    }
}
