package com.elazarev.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 13.02.18
 */
@Entity(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
