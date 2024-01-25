package com.example.board.dto;

import java.time.LocalDateTime;

import com.example.board.domain.Article;
import com.example.board.domain.UserAccount;

public record ArticleDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static ArticleDto of(
            final UserAccountDto userAccountDto,
            final String title,
            final String content,
            final String hashtag
    ) {
        return new ArticleDto(null, userAccountDto, title, content, hashtag, null, null, null, null);
    }

    public static ArticleDto of(
            final Long id,
            final UserAccountDto userAccountDto,
            final String title,
            final String content,
            final String hashtag,
            final LocalDateTime createdAt,
            final String createdBy,
            final LocalDateTime modifiedAt,
            final String modifiedBy
    ) {
        return new ArticleDto(
                id,
                userAccountDto,
                title,
                content,
                hashtag,
                createdAt,
                createdBy,
                modifiedAt,
                modifiedBy
        );
    }

    public static ArticleDto from(final Article entity) {
        return new ArticleDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public Article toEntity(final UserAccount userAccount) {
        return Article.of(
                userAccount,
                title,
                content,
                hashtag
        );
    }

}
