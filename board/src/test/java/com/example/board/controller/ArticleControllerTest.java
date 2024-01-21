package com.example.board.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.board.config.SecurityConfig;
import com.example.board.domain.type.SearchType;
import com.example.board.dto.ArticleWithCommentsDto;
import com.example.board.dto.UserAccountDto;
import com.example.board.service.ArticleService;
import com.example.board.service.PaginationService;

@DisplayName("View 컨트롤러 테스트 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService; // MockBean은 생성자 주입 지원 X
    @MockBean private PaginationService paginationService;

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
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt()))
                .willReturn(List.of(0, 1, 2, 3, 4));

        // when & then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));

        then(articleService).should()
                .searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should()
                .getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 페이지 검색어와 함께 호출")
    @Test
    void 게시글_리스트_페이지_검색어() throws Exception {
        // given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        given(articleService.searchArticles(eq(searchType), eq(searchValue), any(Pageable.class)))
                .willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt()))
                .willReturn(List.of(0, 1, 2, 3, 4));

        // when & then
        mvc.perform(get("/articles")
                        .queryParam("searchType", searchType.name())
                        .queryParam("searchValue", searchValue)
                )
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes"));

        then(articleService).should()
                .searchArticles(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should()
                .getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 페이지 (페이징, 정렬 기능) 정상 호출")
    @Test
    void 게시글_리스트_페이지_페이징_정렬() throws Exception {
        // given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);

        given(articleService.searchArticles(null, null, pageable))
                .willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages()))
                .willReturn(barNumbers);

        // when & then
        mvc.perform(get("/articles")
                        .queryParam("page", String.valueOf(pageNumber))
                        .queryParam("size", String.valueOf(pageSize))
                        .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));

        then(articleService).should()
                .searchArticles(null, null, pageable);
        then(paginationService).should()
                .getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }

    @DisplayName("[view][GET] 게시글 리스트 상세 페이지 정상 호출")
    @Test
    void 게시글_리스트_상세_페이지() throws Exception {
        // given
        Long articleId = 1L;
        Long totalCount = 1L;

        given(articleService.getArticle(articleId))
                .willReturn(createArticleWithCommentsDto());
        given(articleService.getArticleCount())
                .willReturn(totalCount);

        // when & then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attribute("totalCount", totalCount));

        then(articleService).should()
                .getArticle(articleId);
        then(articleService).should()
                .getArticleCount();
    }

    @Disabled
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 정상 호출")
    @Test
    void 게시글_검색_페이지() throws Exception {
        // given

        // when & then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(view().name("/articles/search"));
    }

    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 정상 호출, 검색어 X")
    @Test
    void 게시글_해시태그_검색_페이지() throws Exception {
        // given
        List<String> hashtags = List.of("#java", "#spring", "#boot");
        given(articleService.searchArticlesViaHashtag(eq(null), any(Pageable.class)))
                .willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt()))
                .willReturn(List.of(1, 2, 3, 4, 5));
        given(articleService.getHashtags())
                .willReturn(hashtags);

        // when & then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles", Page.empty()))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG));
        then(articleService).should().searchArticlesViaHashtag(eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
        then(articleService).should().getHashtags();
    }

    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 정상 호출, 검색어 O")
    @Test
    void 게시글_해시태그_검색_페이지_입력() throws Exception {
        // given
        String hashtag = "#java";
        List<String> hashtags = List.of("#java", "#spring", "#boot");

        given(articleService.searchArticlesViaHashtag(eq(hashtag), any(Pageable.class)))
                .willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt()))
                .willReturn(List.of(1, 2, 3, 4, 5));
        given(articleService.getHashtags())
                .willReturn(hashtags);

        // when & then
        mvc.perform(get("/articles/search-hashtag")
                        .queryParam("searchValue", hashtag)
                )
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // HTML 파일
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles", Page.empty()))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG));
        then(articleService).should().searchArticlesViaHashtag(eq(hashtag), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
        then(articleService).should().getHashtags();
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
