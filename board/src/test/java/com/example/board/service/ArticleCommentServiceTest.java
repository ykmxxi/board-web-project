package com.example.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.board.domain.Article;
import com.example.board.domain.ArticleComment;
import com.example.board.dto.ArticleCommentDto;
import com.example.board.repository.ArticleCommentRepository;
import com.example.board.repository.ArticleRepository;

@DisplayName("비즈니스 로직 : 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService service;
    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private ArticleRepository articleRepository;

    @DisplayName("댓글 리스트 조회 기능: 게시글 ID를 주면 해당 게시글의 댓글 리스트를 반환")
    @Test
    void 게시글_댓글_리스트_조회() {
        // given
        long articleId = 1L;
        given(articleRepository.findById(articleId))
                .willReturn(Optional.of(Article.of("title", "content", "#java")));

        // when
        List<ArticleCommentDto> comments = service.searchArticleComments(articleId);

        // then
        assertThat(comments).isNotNull();
        then(articleRepository).should()
                .findById(articleId);
    }

    @DisplayName("댓글 저장 기능: 댓글 정보를 입력하면 댓글 저장")
    @Test
    void 게시글_댓글_저장() {
        // given
        given(articleCommentRepository.save(any(ArticleComment.class)))
                .willReturn(null);

        // when
        service.saveArticleComment(
                ArticleCommentDto.of(LocalDateTime.now(), "Uno", LocalDateTime.now(), "Uno", "comment"));

        // then
        then(articleCommentRepository).should()
                .save(any(ArticleComment.class));
    }

}
