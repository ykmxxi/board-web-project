package com.example.board.dto;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.example.board.domain.ArticleComment}
 */
public record ArticleCommentDto(
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy,
        String content
) {


    public static ArticleCommentDto of(
            final LocalDateTime createdAt,
            final String createdBy,
            final LocalDateTime modifiedAt,
            final String modifiedBy,
            final String content
    ) {
        return new ArticleCommentDto(createdAt, createdBy, modifiedAt, modifiedBy, content);
    }

}
