package com.elazarev.service;

import com.elazarev.domain.Question;
import com.elazarev.domain.Tag;
import com.elazarev.domain.User;
import com.elazarev.exceptions.ForbiddenResourceException;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.exceptions.UserAlreadyExistsException;
import com.elazarev.repository.RoleRepository;
import com.elazarev.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test class for UserService class.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 26.02.18
 */
public class UserServiceTest {
    /**
     * Object to test.
     */
    private UserService userService;
    /**
     * Mock if UserRepository.
     */
    private UserRepository userRepository;
    /**
     * Mock of RoleRepository.
     */
    private RoleRepository roleRepository;
    /**
     * Mock of TagService.
     */
    private TagsService tagsService;

    /**
     * SetUp method for each test.
     * @throws Exception if error occur.
     */
    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        tagsService = mock(TagsService.class);
        userService = new UserService(userRepository, roleRepository, tagsService);
    }

    /**
     * Test for findByLogin() method.
     * @throws Exception if test filed.
     */
    @Test(expected = ResourceNotFoundException.class)
    public void findByLogin() throws Exception {

        User admin = new User();
        admin.setLogin("Admin");

        User user = new User();
        admin.setLogin("User");

        when(userRepository.findUserByLogin(anyString())).thenReturn(Optional.ofNullable(null));
        when(userRepository.findUserByLogin("admin")).thenReturn(Optional.of(admin));
        when(userRepository.findUserByLogin("user")).thenReturn(Optional.of(user));

        assertThat(userService.findByLogin("admin"), is(admin));
        assertThat(userService.findByLogin("user"), is(user));
        userService.findByLogin("someName");
    }

    /**
     * Test for getUser() method.
     * @throws Exception if test filed.
     */
    @Test(expected = ForbiddenResourceException.class)
    public void getUser() throws Exception {
        User admin = new User();
        admin.setLogin("Admin");

        User user = new User();
        admin.setLogin("User");

        when(userRepository.findUserByLogin(anyString())).thenReturn(Optional.ofNullable(null));
        when(userRepository.findUserByLogin("admin")).thenReturn(Optional.of(admin));
        when(userRepository.findUserByLogin("user")).thenReturn(Optional.of(user));

        Principal adminPrin = new PrincipalImpl("admin");
        Principal userPrin = new PrincipalImpl("admin");


        assertThat(userService.getUser(adminPrin), is(admin));
        assertThat(userService.getUser(userPrin), is(user));
        userService.getUser(null);
    }

    /**
     * Test for getPage() method.
     */
    @Test(expected = ResourceNotFoundException.class)
    public void getPage() {

        List<User> users = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            users.add(new User());
        }

        PageRequest req1 = PageRequest.of(0, UserService.MAX_USERS_PER_PAGE, Sort.Direction.DESC, "id");
        PageRequest req2 = PageRequest.of(1, UserService.MAX_USERS_PER_PAGE, Sort.Direction.DESC, "id");
        PageRequest req3 = PageRequest.of(2, UserService.MAX_USERS_PER_PAGE, Sort.Direction.DESC, "id");

        Page<User> page1 = new PageImpl<>(users.subList(0, UserService.MAX_USERS_PER_PAGE));
        Page<User> page2 = new PageImpl<>(users.subList(UserService.MAX_USERS_PER_PAGE, users.size()));
        Page<User> emptyPage = new PageImpl<>(Arrays.asList());

        when(userRepository.findAll(eq(req1))).thenReturn(page1);
        when(userRepository.findAll(eq(req2))).thenReturn(page1);
        when(userRepository.findAll(eq(req3))).thenReturn(emptyPage);

        Page<User> res1 = userService.getPage(Optional.of(1));
        assertThat(res1, is(page1));
        assertTrue(res1.hasContent());

        Page<User> res2 = userService.getPage(Optional.of(2));
        assertThat(res2, is(page2));
        assertTrue(res2.hasContent());

        assertThat(userService.getPage(Optional.ofNullable(null)), is(page1));

        userService.getPage(Optional.of(100));
    }

    /**
     * Test for createUser() method.
     */
    @Test(expected = UserAlreadyExistsException.class)
    public void createUser() {

        User admin = new User();
        admin.setLogin("admin");

        User userToCreate = new User();
        userToCreate.setLogin("NewUser");

        User createdUser = new User();
        createdUser.setLogin("NewUser");
        createdUser.setId(9009L);

        when(userRepository.findUserByLogin(anyString())).thenReturn(Optional.ofNullable(null));
        when(userRepository.findUserByLogin("admin")).thenReturn(Optional.of(admin));
        when(userRepository.save(userToCreate)).thenReturn(createdUser);

        User res = userService.createUser(userToCreate);
        assertThat(res, is(createdUser));
        assertThat(res.getId(), is(not(0L)));

        userService.createUser(admin); // should throw exception
    }

    /**
     * Test for saveOnlyChangedFields() method.
     */
    @Test
    public void saveOnlyChangedFields() {

        User userInBD = new User();
        userInBD.setId(1L);
        userInBD.setLogin("user");
        userInBD.setEmail("user@mail.ru");
        userInBD.setLastName("Ivanov");
        userInBD.setFirstName("Ivan");
        userInBD.setPassword("123");
        userInBD.setPhotoUrl("");
        userInBD.setAbout("");

        when(userRepository.findUserByLogin("user")).thenReturn(Optional.of(userInBD));

        User userToSave = new User();
        userToSave.setLogin("user");
        userToSave.setEmail("newmail@mail.ru");
        userToSave.setLastName("Ivanov");
        userToSave.setFirstName("Ivan");
        userToSave.setPhotoUrl("");
        userToSave.setAbout("");

        Principal principal = new PrincipalImpl("user");
        userService.saveOnlyChangedFields(userToSave, principal);

        assertThat(userInBD.getFirstName(), is("Ivan"));
        assertThat(userInBD.getPassword(), is("123"));
        assertThat(userInBD.getEmail(), is("newmail@mail.ru"));

        verify(userRepository).save(userInBD);
    }

    /**
     * Test for isUsersQuestion() method.
     */
    @Test
    public void isUsersQuestion() {
        User user = new User();
        user.setLogin("user");

        Question q1 = new Question();
        q1.setAuthor(user);

        Question q2 = new Question();
        q2.setAuthor(new User());

        Principal principal = new PrincipalImpl("user");

        when(userRepository.findUserByLogin("user")).thenReturn(Optional.of(user));

        assertThat(userService.isUsersQuestion(q1, principal), is(true));
        assertThat(userService.isUsersQuestion(q2, principal), is(false));
    }

    /**
     * Test for subscribeToTag() method.
     */
    @Test
    public void subscribeToTag() {

        User user = new User();
        user.setLogin("user");

        Tag java = new Tag("java");

        Principal principal = new PrincipalImpl("user");

        when(tagsService.getTagByName("java")).thenReturn(java);
        when(userRepository.findUserByLogin("user")).thenReturn(Optional.of(user));

        assertThat(userService.subscribeToTag(principal, "java"), is(true));
        assertThat(userService.subscribeToTag(principal, "java"), is(false));

        verify(userRepository).save(user);
    }
}
