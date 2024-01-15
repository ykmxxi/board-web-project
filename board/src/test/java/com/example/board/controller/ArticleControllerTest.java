package com.example.board.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.board.config.SecurityConfig;
import com.example.board.dto.ArticleWithCommentsDto;
import com.example.board.dto.UserAccountDto;
import com.example.board.service.ArticleService;

@DisplayName("View 컨트롤러 테스트 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService; // MockBean은 생성자 주입 지원 X

    // 테스트 코드는 생성자가 1개여도 @Autowired 꼭 붙여야함
    public ArticleControllerTest(@Autowired final MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 페이지 정상 호출")
    @Test
    void 게시글_리스트_페이지() throws Exception {
        // given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class)))
                .willReturn(Page.empty());

        // when & then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"));

        then(articleService).should()
                .searchArticles(eq(null), eq(null), any(Pageable.class));
    }

    @DisplayName("[view][GET] 게시글 리스트 상세 페이지 정상 호출")
    @Test
    void 게시글_리스트_상세_페이지() throws Exception {
        // given
        Long articleId = 1L;
        given(articleService.getArticle(articleId))
                .willReturn(createArticleWithCommentsDto());

        // when & then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));

        then(articleService).should()
                .getArticle(articleId);
    }

    @Disabled
    @DisplayName("[view][GET] 게시글 검색 페이지 정상 호출")
    @Test
    void 게시글_검색_페이지() throws Exception {
        // given

        // when & then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(model().attributeExists("/articles/search"));
    }

    @Disabled
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 정상 호출")
    @Test
    void 게시글_해시태그_검색_페이지() throws Exception {
        // given

        // when & then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(model().attributeExists("articles/search-hashtag"));
    }

    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "ykmxxi",
                LocalDateTime.now(),
                "ykmxxi"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "ykmxxi",
                "pw",
                "ykmxxi@email.com",
                "ykmxxi",
                "memo",
                LocalDateTime.now(),
                "ykmxxi",
                LocalDateTime.now(),
                "ykmxxi"
        );
    }

}
