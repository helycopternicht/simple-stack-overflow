package com.elazarev.repository;

import com.elazarev.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for users table.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    /**
     * Find user by specified login.
     * @param name user name.
     * @return user in optional wrapper.
     */
    Optional<User> findUserByLogin(String name);
}
