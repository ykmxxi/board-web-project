package com.example.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.domain.Article;
import com.example.board.domain.UserAccount;
import com.example.board.domain.constant.SearchType;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.ArticleWithCommentsDto;
import com.example.board.repository.ArticleRepository;
import com.example.board.repository.UserAccountRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;

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
            case ID ->
                    articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable)
                    .map(ArticleDto::from);
            case HASHTAG ->
                    articleRepository.findByHashtagContaining("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(final String hashtag, final Pageable pageable) {
        if (hashtag == null || hashtag.isBlank()) {
            return Page.empty(pageable);
        }
        return articleRepository.findByHashtagContaining(hashtag, pageable)
                .map(ArticleDto::from);
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void saveArticle(final ArticleDto articleDto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(articleDto.userAccountDto().userId());
        articleRepository.save(articleDto.toEntity(userAccount));
    }

    public void updateArticle(final Long articleId, final ArticleDto articleDto) {
        try {
            Article article = articleRepository.findById(articleId)
                    .orElseThrow();
            UserAccount userAccount = userAccountRepository.getReferenceById(articleDto.userAccountDto().userId());

            if (article.getUserAccount().equals(userAccount)) {
                if (articleDto.title() != null) {
                    article.setTitle(articleDto.title());
                }
                if (articleDto.content() != null) {
                    article.setContent(articleDto.content());
                }
                article.setHashtag(articleDto.hashtag());
            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 수정에 필요한 정보를 찾을 수 없습니다. {}", e.getLocalizedMessage());
        }
    }

    public void deleteArticle(final Long articleId, final String userId) {
        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }

}
