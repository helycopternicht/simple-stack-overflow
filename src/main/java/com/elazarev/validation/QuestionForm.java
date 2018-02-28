package com.elazarev.validation;

import com.elazarev.domain.Tag;
import com.elazarev.domain.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 28.02.18
 */
public class QuestionForm {

    private Long id;

    private User author;

    private LocalDateTime createDate;

    private String title;

    private String description;

    private Boolean closed;

    private Set<Tag> tags = new TreeSet<>();

}
