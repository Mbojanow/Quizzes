package com.bocian.quizzes.controllers.v1;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageRequestParamsFactory {

    public static String get(final int page, final int size) {
        return "?page=" + page + "&size=" + size;
    }

    public static String getAll() {
        return "?page=0&size=" + Integer.MAX_VALUE;
    }
}
