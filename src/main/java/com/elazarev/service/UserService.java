package com.elazarev.service;

import com.elazarev.domain.User;
import com.elazarev.repository.RoleRepository;
import com.elazarev.repository.UserRepository;
import com.elazarev.security.excepttions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }

    public User findUserByLogin(String name) {
        return repo.findUserByLogin(name);
    }

    public User createUser(User u) throws UserAlreadyExistsException {
        if (repo.findUserByLogin(u.getLogin()) != null) {
            throw new UserAlreadyExistsException("User already exists - login: " + u.getLogin());
        }
        u.getRoles().add(roleRepo.findByName("role_user"));
        return repo.save(u);
    }
}
