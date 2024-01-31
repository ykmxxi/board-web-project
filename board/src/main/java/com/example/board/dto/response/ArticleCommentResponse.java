package com.example.board.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.board.dto.ArticleCommentDto;

public record ArticleCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        String email,
        String nickname,
        String userId
) implements Serializable {

    public static ArticleCommentResponse of(
            final Long id,
            final String content,
            final LocalDateTime createdAt,
            final String email,
            final String nickname,
            final String userId
    ) {
        return new ArticleCommentResponse(id, content, createdAt, email, nickname, userId);
    }

    public static ArticleCommentResponse from(final ArticleCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleCommentResponse(
                dto.id(),
                dto.content(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().userId()
        );
    }

}
