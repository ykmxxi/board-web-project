package com.example.board.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.domain.type.SearchType;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.ArticleUpdateDto;
import com.example.board.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(final SearchType title, final String searchKeyword) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleDto searchArticle(final long articleId) {
        return null;
    }

    public void saveArticle(final ArticleDto articleDto) {
    }

    public void updateArticle(final long articleId, final ArticleUpdateDto articleUpdateDto) {
    }

    public void deleteArticle(final long articleId) {
    }

}
