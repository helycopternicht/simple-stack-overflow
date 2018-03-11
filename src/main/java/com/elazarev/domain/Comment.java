package com.elazarev.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Comment to answer model.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Entity
@Table(name = "comments")
public class Comment {
    /**
     * Unique identifiator.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The answer for which the comment is intended.
     */
    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;
    /**
     * Comment author.
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    /**
     * Comment create date.
     */
    @Column(name = "create_date")
    @GeneratedValue
    private LocalDateTime createDate;
    /**
     * Comment test.
     */
    @Column(name = "text", nullable = false)
    private String text;

    /**
     * Empty constructor.
     */
    public Comment() {
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
     * Getter for answer field.
     * @return answer.
     */
    public Answer getAnswer() {
        return answer;
    }

    /**
     * Setter for answer field.
     * @param answer new answer.
     */
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    /**
     * Getter for author field.
     * @return author.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Setter for answer field.
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
     * @param createDate new create date.
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Getter for text field.
     * @return text.
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
}
