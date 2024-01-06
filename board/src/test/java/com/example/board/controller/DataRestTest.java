package com.example.board.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Controller integration 테스트
 * - DB에 영향을 주는 테스트기 때문에 @Transactional 추가
 * - 비용이 큰 테스트
 */
@DisplayName("Data REST Api 테스트")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class DataRestTest {

    private final MockMvc mvc;

    DataRestTest(@Autowired final MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("게시글 리스트 조회")
    @Test
    void 게시글_리스트_조회() throws Exception {
        // given

        // when & then
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("게시글 단건 조회")
    @Test
    void 게시글_단건_조회() throws Exception {
        // given

        // when & then
        mvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("게시글 댓글 리스트 조회")
    @Test
    void 게시글_댓글_리스트_조회() throws Exception {
        // given

        // when & then
        mvc.perform(get("/api/articles/1/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("댓글 리스트 조회")
    @Test
    void 댓글_리스트_조회() throws Exception {
        // given

        // when & then
        mvc.perform(get("/api/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("댓글 단건 조회")
    @Test
    void 댓글_단건_조회() throws Exception {
        // given

        // when & then
        mvc.perform(get("/api/articleComments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

}
