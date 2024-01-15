package com.example.board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.domain.Article;
import com.example.board.domain.type.SearchType;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.ArticleWithCommentsDto;
import com.example.board.repository.ArticleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(
            final SearchType searchType,
            final String searchKeyword,
            final Pageable pageable
    ) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable)
                    .map(ArticleDto::from);
        }

        return switch (searchType) {
            case TITLE -> articleRepository.findByTitle(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtagContaining("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void saveArticle(final ArticleDto articleDto) {
        articleRepository.save(articleDto.toEntity());
    }

    public void updateArticle(final ArticleDto articleDto) {
        try {
            Article article = articleRepository.getReferenceById(articleDto.id());
            if (articleDto.title() != null) {
                article.setTitle(articleDto.title());
            }
            if (articleDto.content() != null) {
                article.setContent(articleDto.content());
            }
            if (articleDto.hashtag() != null) {
                article.setHashtag(articleDto.hashtag());
            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 존재하지 않는 게시글 입니다. dto: {}", articleDto);
        }
    }

    public void deleteArticle(final Long articleId) {
        articleRepository.deleteById(articleId);
    }

}
