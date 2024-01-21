package com.example.board.dto.request;

import com.example.board.dto.ArticleCommentDto;
import com.example.board.dto.UserAccountDto;

public record ArticleCommentRequest(
        Long articleId,
        String content
) {

    public static ArticleCommentRequest of(final Long articleId, final String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(final UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
                articleId,
                userAccountDto,
                content
        );
    }

}
