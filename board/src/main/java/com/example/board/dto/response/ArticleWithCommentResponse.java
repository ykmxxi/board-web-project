package com.example.board.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.board.dto.ArticleWithCommentsDto;

public record ArticleWithCommentResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String email,
        String nickname,
        Set<ArticleCommentResponse> articleCommentResponses
) implements Serializable {

    public static ArticleWithCommentResponse of(
            final Long id,
            final String title,
            final String content,
            final String hashtag,
            final LocalDateTime createdAt,
            final String email,
            final String nickname,
            final Set<ArticleCommentResponse> articleCommentResponses
    ) {
        return new ArticleWithCommentResponse(
                id,
                title,
                content,
                hashtag,
                createdAt,
                email,
                nickname,
                articleCommentResponses
        );
    }

    public static ArticleWithCommentResponse from(final ArticleWithCommentsDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleWithCommentResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                dto.articleCommentDtos().stream()
                        .map(ArticleCommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

}
