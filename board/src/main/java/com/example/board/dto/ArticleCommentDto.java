package com.example.board.dto;

import java.time.LocalDateTime;

import com.example.board.domain.Article;
import com.example.board.domain.ArticleComment;

/**
 * DTO for {@link com.example.board.domain.ArticleComment}
 */
public record ArticleCommentDto(
        Long id,
        Long articleId,
        UserAccountDto userAccountDto,
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static ArticleCommentDto of(
            final Long id,
            final Long articleId,
            final UserAccountDto userAccountDto,
            final String content,
            final LocalDateTime createdAt,
            final String createdBy,
            final LocalDateTime modifiedAt,
            final String modifiedBy
    ) {
        return new ArticleCommentDto(
                id,
                articleId,
                userAccountDto,
                content,
                createdAt,
                createdBy,
                modifiedAt,
                modifiedBy
        );
    }

    public static ArticleCommentDto from(final ArticleComment entity) {
        return new ArticleCommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public ArticleComment toEntity(final Article entity) {
        return ArticleComment.of(
                entity,
                userAccountDto.toEntity(),
                content
        );
    }

}
