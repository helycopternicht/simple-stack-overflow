package com.elazarev.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Model for complaint on question or answer.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Entity
@Table(name = "complaints")
public class Complaint {
    /**
     * Unique identificator.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The question on which they complainedю
     */
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    /**
     * The answer on which they complained.
     */
    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;
    /**
     * Complaint create date.
     */
    @Column(name = "create_date")
    @GeneratedValue
    private LocalDateTime createDate;
    /**
     * Author of complaint.
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    /**
     * Complaint text.
     */
    @Column(name = "text", nullable = false)
    private String text;

    /**
     * Empty constructor.
     */
    public Complaint() {
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
     * Getter for created date field.
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
