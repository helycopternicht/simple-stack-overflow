package com.elazarev.repository;

import com.elazarev.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findUserByLogin(String name);
}
