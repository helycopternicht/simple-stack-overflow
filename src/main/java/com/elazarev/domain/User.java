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

    /**
     * Returns true if user subscribed to specified tag.
     * @param t tag to check.
     * @return true or false.
     */
    public boolean subscribedToTag(Tag t) {
        for (Tag tag : tags) {
            if (tag.equals(t)) {
                return true;
            }
        }
        return false;
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
     * Getter for login field.
     * @return login.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Setter for id field.
     * @param login new login.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Returns collections of sers roles.
     * @return roles.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /**
     * Getter for password field.
     * @return password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password field.
     * @param password new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for email field.
     * @return email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email field.
     * @param email new email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for first name field.
     * @return first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for first name field.
     * @param firstName new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for last name field.
     * @return last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for last name field.
     * @param lastName new last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for about field.
     * @return about.
     */
    public String getAbout() {
        return about;
    }

    /**
     * Setter for about field.
     * @param about new about.
     */
    public void setAbout(String about) {
        this.about = about;
    }

    /**
     * Getter for photo url field.
     * @return photo url.
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * Setter for photo url field.
     * @param photoUrl new photo url.
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * Getter for banned field.
     * @return banned.
     */
    public Boolean getBaned() {
        return baned;
    }

    /**
     * Setter for banned field.
     * @param baned banned.
     */
    public void setBaned(Boolean baned) {
        this.baned = baned;
    }

    /**
     * Getter for roles field.
     * @return roles.
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Setter for roles field.
     * @param roles new roles.
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Getter for tags field.
     * @return tags.
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Setter for tags field.
     * @param tags new tags.
     */
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Getter for answers field.
     * @return answers.
     */
    public Set<Answer> getAnswers() {
        return answers;
    }

    /**
     * Setter for answers field.
     * @param answers new asnwers.
     */
    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    /**
     * Getter for subscriptions field.
     * @return subscriptions.
     */
    public Set<Question> getSubscriptions() {
        return subscriptions;
    }

    /**
     * Setter for subscriptions field.
     * @param subscriptions new subscriptions.
     */
    public void setSubscriptions(Set<Question> subscriptions) {
        this.subscriptions = subscriptions;
    }

    /**
     * Getter for questions field.
     * @return questions.
     */
    public Set<Question> getQuestions() {
        return questions;
    }

    /**
     * Setter for questions field.
     * @param questions new questions.
     */
    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    /**
     * Returns login.
     * @return login.
     */
    @Override
    public String getUsername() {
        return login;
    }

    /**
     * Returns true if user isn't banned.
     * @return true of false.
     */
    @Override
    public boolean isAccountNonExpired() {
        return !baned;
    }

    /**
     * Returns true if user isn't banned.
     * @return true of false.
     */
    @Override
    public boolean isAccountNonLocked() {
        return !baned;
    }

    /**
     * Returns true if user isn't banned.
     * @return true of false.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return !baned;
    }

    /**
     * Returns true if user isn't banned.
     * @return true of false.
     */
    @Override
    public boolean isEnabled() {
        return !baned;
    }

    /**
     * Equals method based on id.
     * @param o object to compare.
     * @return true if objects are equals.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getId() != null ? getId().equals(user.getId()) : user.getId() == null;
    }

    /**
     * Hash code method based on id.
     * @return hash code.
     */
    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    /**
     * Ti string method.
     * @return string representations of user based on login.
     */
    @Override
    public String toString() {
        return "@" + this.login;
    }
}
