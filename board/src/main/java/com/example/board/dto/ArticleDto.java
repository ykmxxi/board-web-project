package com.example.board.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleDto(
        LocalDateTime createdAt,
        String createdBy,
        String title,
        String content,
        String hashtag
) implements Serializable {

    public static ArticleDto of(
            final LocalDateTime createdAt,
            final String createdBy,
            final String title,
            final String content,
            final String hashtag
    ) {
        return new ArticleDto(createdAt, createdBy, title, content, hashtag);
    }

}
