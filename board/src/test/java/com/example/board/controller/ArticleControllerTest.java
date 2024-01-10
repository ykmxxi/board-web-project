package com.example.board.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.board.config.SecurityConfig;

@DisplayName("View 컨트롤러 테스트 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    // 테스트 코드는 생성자가 1개여도 @Autowired 꼭 붙여야함
    public ArticleControllerTest(@Autowired final MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 페이지 정상 호출")
    @Test
    void 게시글_리스트_페이지() throws Exception {
        // given

        // when & then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"));
    }

    @DisplayName("[view][GET] 게시글 리스트 상세 페이지 정상 호출")
    @Test
    void 게시글_리스트_상세_페이지() throws Exception {
        // given

        // when & then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));
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

}
