package com.example.board.dto.request;

import com.example.board.dto.ArticleDto;
import com.example.board.dto.UserAccountDto;

public record ArticleRequest(
        String title,
        String content,
        String hashtag
) {

    public static ArticleRequest of(
            final String title,
            final String content,
            final String hashtag
    ) {
        return new ArticleRequest(title, content, hashtag);
    }

    public ArticleDto toDto(final UserAccountDto userAccountDto) {
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtag
        );
    }

}
