package com.example.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import com.example.board.domain.Article;
import com.example.board.domain.type.SearchType;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.ArticleUpdateDto;
import com.example.board.repository.ArticleRepository;

@DisplayName("비즈니스 로직 : 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks private ArticleService service; // mock 주입 대상
    @Mock private ArticleRepository articleRepository; // mock

    @DisplayName("게시글 검색 기능: 검색 파라미터가 주어지면 조건에 맞는 게시글 리스트 반환 + 페이지네이션")
    @Test
    void 게시글_검색_기능() {
        // when
        Page<ArticleDto> articles = service.searchArticles(SearchType.TITLE, "search keyword"); // 제목, 본문, ID, 닉네임, 해시태그

        // then
        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글 페이지 이동 기능: 게시글을 조회하면 게시글 페이지로 이동")
    @Test
    void 게시글_페이지_이동_기능() {
        // when
        ArticleDto article = service.searchArticle(1L);

        // then
        assertThat(article).isNotNull();
    }

    @DisplayName("게시글 생성 기능: 게시글 정보를 입력하면 게시글 생성")
    @Test
    void 게시글_생성() {
        // given
        given(articleRepository.save(any(Article.class)))
                .willReturn(null);

        // when
        service.saveArticle(ArticleDto.of(LocalDateTime.now(), "ykmxxi", "title", "content", "#java"));

        // then
        then(articleRepository).should()
                .save(any(Article.class));
    }

    @DisplayName("게시글 수정 기능: 게시글 ID와 수정 정보를 입력하면 게시글 수정")
    @Test
    void 게시글_수정() {
        // given
        given(articleRepository.save(any(Article.class)))
                .willReturn(null);

        // when
        service.updateArticle(1L, ArticleUpdateDto.of("title", "content", "#java"));

        // then
        then(articleRepository).should()
                .save(any(Article.class));
    }

    @DisplayName("게시글 삭제 기능: 게시글 ID를 입력하면 게시글 삭제")
    @Test
    void 게시글_삭제() {
        // given
        willDoNothing().given(articleRepository)
                .delete(any(Article.class));

        // when
        service.deleteArticle(1L);

        // then
        then(articleRepository).should()
                .delete(any(Article.class));
    }

}
