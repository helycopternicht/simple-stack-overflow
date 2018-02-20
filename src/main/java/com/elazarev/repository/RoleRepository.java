package com.elazarev.repository;

import com.elazarev.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String n);
}
