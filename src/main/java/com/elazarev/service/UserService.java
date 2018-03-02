package com.elazarev.service;

import com.elazarev.domain.Question;
import com.elazarev.domain.Tag;
import com.elazarev.domain.User;
import com.elazarev.exceptions.ForbiddenResourceExceprion;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.repository.RoleRepository;
import com.elazarev.repository.UserRepository;
import com.elazarev.exceptions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Service
public class UserService {

    public static final int MAX_USERS_PER_PAGE = 10;

    private UserRepository repo;

    private RoleRepository roleRepo;

    private TagsService tagsService;

    @Autowired
    public UserService(UserRepository repo, RoleRepository roleRepo, TagsService tagsService) {
        this.repo = repo;
        this.roleRepo = roleRepo;
        this.tagsService = tagsService;
    }

    public User findByLogin(String name) throws ResourceNotFoundException {
        Optional<User> user = repo.findUserByLogin(name);
        return user.orElseThrow(() -> new ResourceNotFoundException("User with name " + name + "not found"));
    }

    public User getUser(Principal p) throws ForbiddenResourceExceprion {
        if (p == null) {
            throw new ForbiddenResourceExceprion("You are not authorized");
        }
        return findByLogin(p.getName());
    }

    public Page<User> getPage(Optional<Integer> page) throws ResourceNotFoundException {
        Pageable p = PageRequest.of(page.orElse(1) - 1, MAX_USERS_PER_PAGE, Sort.Direction.DESC, "id");
        Page<User> currentPage = repo.findAll(p);
        if (currentPage.getNumberOfElements() == 0) {
            throw new ResourceNotFoundException("page not found");
        }
        return currentPage;
    }

    public User createUser(User u) throws UserAlreadyExistsException {
        if (repo.findUserByLogin(u.getLogin()) != null) {
            throw new UserAlreadyExistsException("User already exists - login: " + u.getLogin());
        }
        u.getRoles().add(roleRepo.findByName(RoleRepository.ROLE_NAME_USER));
        return repo.save(u);
    }

    public void save(User u) {
        repo.save(u);
    }

    public void saveOnlyChangedFields(User source, Principal p) {
        User receiver = getUser(p);

        if (!receiver.getFirstName().equals(source.getFirstName())) {
            receiver.setFirstName(source.getFirstName());
        }
        if (!receiver.getLastName().equals(source.getLastName())) {
            receiver.setLastName(source.getLastName());
        }

        if (!receiver.getPhotoUrl().equals(source.getPhotoUrl())) {
            receiver.setPhotoUrl(source.getPhotoUrl());
        }

        if (!receiver.getEmail().equals(source.getEmail())) {
            receiver.setEmail(source.getEmail());
        }

        if (!receiver.getAbout().equals(source.getAbout())) {
            receiver.setAbout(source.getAbout());
        }
        save(receiver);
    }

    public boolean isQuestionOfUser(Question q, Principal u) {
        if (u == null) {
            return false;
        }

        User user = getUser(u);
        if (user.equals(q.getAuthor())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean subscribe(Principal p, String tagName) {
        Tag tag = tagsService.getTagByName(tagName);
        User user = getUser(p);

        if (user.subscribedToTag(tag)) {
            return false;
        }

        user.getTags().add(tag);
        save(user);
        return true;
    }

}
