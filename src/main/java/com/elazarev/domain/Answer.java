package com.elazarev.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * An Answer to a question model. 
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Entity
@Table(name = "answers")
public class Answer {
    /**
     * Unique identificator.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * A question was answered.
     */
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    /**
     * Answer author.
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    /**
     * Answer create date.
     */
    @Column(name = "create_date")
    private LocalDateTime createDate;
    /**
     * Answer text.
     */
    @Column(name = "text")
    private String text;
    /**
     * Is answer solution of question.
     */
    @Column(name = "solution")
    private Boolean solution;
    /**
     * Users who like answer.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "answers_rating", joinColumns = @JoinColumn(name = "answer_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> liked = new HashSet<>();
    /**
     * Answer comments.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "answer", fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<>();

    /**
     * Empty constructor.
     */
    public Answer() {
    }

    /**
     * Returns true if presented user like answer.
     * @param user user.
     * @return true if user like answer, and false else.
     */
    public boolean hasFan(User user) {
        for (User u : this.getLiked()) {
            if (u.equals(user)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Getter for id.
     * @return id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for id.
     * @param id new id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for question field.
     * @return question.
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Setter for question field.
     * @param question new question.
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * Getter for author field.
     * @return author.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Setter for author field.
     * @param author new author.
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Getter for create date field.
     * @return create date.
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Setter for create date field.
     * @param createDate
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Getter for text field.
     * @return text of answer.
     */
    public String getText() {
        return text;
    }

    /**
     * Setter for text field.
     * @param text new text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Getter for solution field.
     * @return solution.
     */
    public Boolean getSolution() {
        return solution;
    }

    /**
     * Setter for solution.
     * @param solution new solution.
     */
    public void setSolution(Boolean solution) {
        this.solution = solution;
    }

    /**
     * Getter for liked field.
     * @return liked collection.
     */
    public Set<User> getLiked() {
        return liked;
    }

    /**
     * Setter for liked field.
     * @param liked new liked.
     */
    public void setLiked(Set<User> liked) {
        this.liked = liked;
    }

    /**
     * Getter for comments field.
     * @return comments.
     */
    public Set<Comment> getComments() {
        return comments;
    }

    /**
     * Setter for comments field.
     * @param comments new comments.
     */
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Equals method based on id compare.
     * @param o object to compare.
     * @return true of false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        return getId().equals(answer.getId());
    }

    /**
     * Hash code mathod based on id.
     * @return hashCode.
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
