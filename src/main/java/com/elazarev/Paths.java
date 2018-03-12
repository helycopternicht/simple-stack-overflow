package com.elazarev;

/**
 * Class to contain all paths in app.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 28.02.18
 */
public class Paths {
    // personal controller
    public static final String PATH_MY_FEED = "/my/feed";
    public static final String PATH_MY_PROFILE = "/my/profile";

    // question controller
    public static final String PATH_QUESTIONS_ALL = "/questions";
    public static final String PATH_QUESTIONS_SEARCH = "/questions/searchPage";
    public static final String PATH_QUESTIONS_SHOW = "/questions/{id}";
    public static final String PATH_QUESTIONS_ADD = "/questions/add";

    public static final String PATH_QUESTIONS_ANSWER_ADD = "/questions/answer";
    public static final String PATH_QUESTIONS_ANSWER_LIKE = "/questions/answer/like";
    public static final String PATH_QUESTIONS_ANSWER_SOLUTION = "/questions/answer/solution";
    public static final String PATH_QUESTIONS_SUBSCRIBE = "/questions/subscribeToTag";

    // tag controller
    public static final String PATH_TAGS_ALL = "/tags";
    public static final String PATH_TAGS_SHOW = "/tags/{name}";
    public static final String PATH_TAGS_SUBSCRIBE = "/tags/subscribeToTag/{name}";

    // user controller
    public static final String PATH_USERS_ALL = "/users";
    public static final String PATH_USERS_SHOW = "/users/{name}";

    // security
    public static final String PATH_LOGIN = "/login";
    public static final String PATH_LOGOUT = "/logout";
    public static final String PATH_REGISTER = "/registration";
}
