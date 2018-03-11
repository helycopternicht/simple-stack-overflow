package com.elazarev.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Application user model.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Entity(name = "users")
public class User implements UserDetails {
    /**
     * Unique identificator.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * User login.
     */
    @Column(name = "login", nullable = false)
    private String login;
    /**
     * User password.
     */
    @Column(name = "password", nullable = false)
    private String password;
    /**
     * User email.
     */
    @Column(name = "email", nullable = false)
    private String email;
    /**
     * First name.
     */
    @Column(name = "fname")
    private String firstName;
    /**
     * Last name.
     */
    @Column(name = "lname")
    private String lastName;
    /**
     * Short text about user.
     */
    @Column(name = "about")
    private String about;
    /**
     * Users profile photo.
     */
    @Column(name = "photo_url")
    private String photoUrl;
    /**
     * Is user locked.
     */
    @Column(name = "baned", nullable = false)
    private Boolean baned;
    /**
     * Set of users roles.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    /**
     * Tags on which user subscribed.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_tags", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();
    /**
     * Questions on which user subscribed.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_subscription", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "question_id"))
    private Set<Question> subscriptions = new HashSet<>();
    /**
     * Users answers.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.EAGER)
    private Set<Answer> answers = new HashSet<>();
    /**
     * Users questions.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.EAGER)
    private Set<Question> questions = new HashSet<>();

    /**
     * Empty constructor.
     */
    public User() {
    }

    public boolean subscribedToTag(Tag t) {
        for (Tag tag : tags) {
            if (tag.equals(t)) {
                return true;
            }
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !baned;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !baned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !baned;
    }

    @Override
    public boolean isEnabled() {
        return !baned;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Boolean getBaned() {
        return baned;
    }

    public void setBaned(Boolean baned) {
        this.baned = baned;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }


    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Set<Question> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Question> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getId() != null ? getId().equals(user.getId()) : user.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "@" + this.login;
    }
}
