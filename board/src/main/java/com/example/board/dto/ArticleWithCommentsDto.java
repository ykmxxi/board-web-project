package com.example.board.dto;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.board.domain.Article;

public record ArticleWithCommentsDto(

        Long id,
        UserAccountDto userAccountDto,
        Set<ArticleCommentDto> articleCommentDtos,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static ArticleWithCommentsDto of(
            final Long id,
            final UserAccountDto userAccountDto,
            final Set<ArticleCommentDto> articleCommentDtos,
            final String title,
            final String content,
            final String hashtag,
            final LocalDateTime createdAt,
            final String createdBy,
            final LocalDateTime modifiedAt,
            final String modifiedBy
    ) {
        return new ArticleWithCommentsDto(
                id,
                userAccountDto,
                articleCommentDtos,
                title,
                content,
                hashtag,
                createdAt,
                createdBy,
                modifiedAt,
                modifiedBy
        );
    }

    public static ArticleWithCommentsDto from(final Article entity) {
        return new ArticleWithCommentsDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getArticleComments().stream()
                        .map(ArticleCommentDto::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

}
