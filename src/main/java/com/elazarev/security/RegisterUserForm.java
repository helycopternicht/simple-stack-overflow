package com.elazarev.security;

import com.elazarev.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Class to validate form of creation new {@link User} user.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 19.02.18
 */
public class RegisterUserForm {
    /**
     * Password encoder. Used for encode user password.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * Users login.
     */
    @NotNull
    @Size(min = 2, max = 30, message = "Login should be longer than 2 characters")
    private String login;
    /**
     * Users password.
     */
    @NotNull
    @Size(min = 3, max = 16)
    private String password;
    /**
     * Users email.
     */
    @NotNull
    @Email
    private String email;
    /**
     * Users first name.
     */
    private String firstName;
    /**
     * Users last name.
     */
    private String lastName;
    /**
     * Short text about user.
     */
    private String about;
    /**
     * Avatar photo url.
     */
    private String photoUrl;

    /**
     * Getter for login field.
     * @return login.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Setter for login field.
     * @param login new login.
     */
    public void setLogin(String login) {
        this.login = login;
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
     * Method creates user {@link User}.
     * @return new user.
     */
    public User constructUser() {
        User user = new User();
        user.setLogin(this.getLogin());
        user.setPassword(passwordEncoder.encode(this.getPassword()));
        user.setEmail(this.getEmail());
        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        user.setAbout(this.getAbout());
        user.setBaned(false);
        user.setPhotoUrl(this.getPhotoUrl());
        return user;
    }
}
