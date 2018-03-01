package com.elazarev.utils;

import com.elazarev.domain.Answer;

import java.util.Comparator;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 01.03.18
 */
public class AnswerComparator implements Comparator<Answer> {
    @Override
    public int compare(Answer o1, Answer o2) {
        if (o1.getSolution() && o2.getSolution()) {
            int likedSize = o2.getLiked().size() - o1.getLiked().size();
            if (likedSize == 0) {
                return (o1.getCreateDate().compareTo(o2.getCreateDate()) * -1);
            } else {
                return likedSize;
            }
        }

        if (o1.getSolution()) {
            return -1;
        } else if (o2.getSolution()) {
            return 1;
        } else {
            return (o1.getCreateDate().compareTo(o2.getCreateDate()) * -1);
        }
    }
}
