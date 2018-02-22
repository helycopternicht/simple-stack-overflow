package com.elazarev.security;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 19.02.18
 */
public class RegisterUserForm {
    @NotNull
    @Size(min = 2, max = 30, message = "Login should be longer than 2 charachters")
    private String login;

    @NotNull
    @Size(min = 3, max = 16)
    private String password;

    @NotNull
    @Email
    private String email;

    private String firstName;

    private String lastName;

    private String about;

    private String photoUrl;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
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
}
