package com.elazarev.repository;

import com.elazarev.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for roles table.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    /**
     * Name of user role in db.
     */
    String ROLE_NAME_USER = "role_user";

    /**
     * Finds role by specified name.
     * @param n name of role.
     * @return role if exists.
     */
    Role findByName(String n);
}
