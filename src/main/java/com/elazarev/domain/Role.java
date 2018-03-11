package com.elazarev.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Users role model.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 13.02.18
 */
@Entity(name = "roles")
public class Role implements GrantedAuthority {
    /**
     * Unique identificator.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Role name.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Empty constructor.
     */
    public Role() {
    }

    /**
     * Getter for id field.
     * @return id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for id field.
     * @param id new id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for name field.
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name field.
     * @param name new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns an authority granted to an {@link Authentication} object.
     * @return authority.
     */
    @Override
    public String getAuthority() {
        return name;
    }
}
