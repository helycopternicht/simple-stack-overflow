package com.elazarev.domain;

import com.elazarev.utils.AnswerComparator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


/**
 * Question model.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Entity
@Table(name = "questions")
public class Question {
    /**
     * Unique identificator.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Question author.
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    /**
     * Create date.
     */
    @Column(name = "create_date", nullable = false)
    @GeneratedValue
    private LocalDateTime createDate;
    /**
     * Question title.
     */
    @Column(name = "title", nullable = false)
    private String title;
    /**
     * Question description.
     */
    @Column(name = "description", nullable = false)
    private String description;
    /**
     * Is question closed.
     */
    @Column(name = "closed", nullable = false)
    private Boolean closed;
    /**
     * Question tags.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question_tags", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @OrderBy("id asc")
    private Set<Tag> tags = new TreeSet<>();
    /**
     * Question subscribers.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_subscription", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> subscribers = new HashSet<>();
    /**
     * Question answers.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question", fetch = FetchType.EAGER)
    private Set<Answer> answers = new TreeSet<>();

    /**
     * Empty constructor.
     */
    public Question() {
    }

    /**
     * Returns true if at least one answer marked as solution.
     * @return true or false.
     */
    public boolean hasSolution() {
        for (Answer a : answers) {
            if (a.getSolution()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns sorted answers.
     * @return answer collection.
     */
    public Collection<Answer> getSortedAnswers() {
        Set<Answer> result = new TreeSet<>(new AnswerComparator());
        result.addAll(answers);
        return result;
    }

    /**
     * Returns true if spesified user is subscribed on question.
     * @param u user to check.
     * @return true or false.
     */
    public boolean userSubscribed(User u) {
        for (User user : this.getSubscribers()) {
            if (user.equals(u)) {
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
     * Getter for author.
     * @return author.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Setter for author.
     * @param author new author.
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Getter for title field.
     * @return title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for titile field.
     * @param title new title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for description field.
     * @return description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description field.
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for closed field.
     * @return
     */
    public Boolean getClosed() {
        return closed;
    }

    /**
     * Setter for closed field.
     * @param closed new closed.
     */
    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    /**
     * Getter for tags field.
     * @return
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
     * @param answers new answers.
     */
    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
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
     * Getter for subscribers field.
     * @return subscribers.
     */
    public Set<User> getSubscribers() {
        return subscribers;
    }

    /**
     * Setter for subscribers field.
     * @param subscribers new subscribers.
     */
    public void setSubscribers(Set<User> subscribers) {
        this.subscribers = subscribers;
    }

    /**
     * Equals method.
     * @param o object to compare.
     * @return true if objects have same class and same id.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return getId() != null ? getId().equals(question.getId()) : question.getId() == null;
    }

    /**
     * Returns hash code based on id.
     * @return hash code.
     */
    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
