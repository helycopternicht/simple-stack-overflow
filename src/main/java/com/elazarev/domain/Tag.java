package com.elazarev.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Entity
@Table(name = "tags")
public class Tag implements Comparable<Tag> {
    /**
     * Unique identificator.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Tag name.
     */
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * Ulr of tag image.
     */
    @Column(name = "image_url")
    private String imageUrl;
    /**
     * Questions with this tag.
     */
    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    private Set<Question> questions = new HashSet<>();
    /**
     * Users subscribed to this tag.
     */
    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();
    /**
     * Short text about tag.
     */
    private String about;

    /**
     * Empty constructor.
     */
    public Tag() {
    }

    /**
     * Constructro with name of tag.
     * @param name name of tag.
     */
    public Tag(String name) {
        this.name = name;
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
     * Getter for image url field.
     * @return image url.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Setter for image url field.
     * @param imageUrl new image url.
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Getter for question field.
     * @return question.
     */
    public Set<Question> getQuestions() {
        return questions;
    }

    /**
     * Setter for question field.
     * @param questions new question.
     */
    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
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
     * Getter for users field.
     * @return users.
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Setter for users field.
     * @param users new users.
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /**
     * Compare to method based on tag name.
     * @param o object to compare.
     * @return order of tag.
     */
    @Override
    public int compareTo(Tag o) {
        return this.getName().compareTo(o.getName());
    }

    /**
     * Equals method based on tag name.
     * @param o object to compare.
     * @return true or false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return getName() != null ? getName().equals(tag.getName()) : tag.getName() == null;
    }

    /**
     * Hash code based on tag name.
     * @return hash code.
     */
    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }
}
