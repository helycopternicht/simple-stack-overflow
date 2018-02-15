package com.elazarev.service;

import com.elazarev.domain.User;
import com.elazarev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public Page<User> doWork(int page) {
         return repo.findAll(PageRequest.of(0, 10));
    }

}
