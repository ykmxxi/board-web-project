package com.example.board.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.board.domain.constant.FormStatus;
import com.example.board.domain.constant.SearchType;
import com.example.board.dto.UserAccountDto;
import com.example.board.dto.request.ArticleRequest;
import com.example.board.dto.response.ArticleResponse;
import com.example.board.dto.response.ArticleWithCommentsResponse;
import com.example.board.service.ArticleService;
import com.example.board.service.PaginationService;

import lombok.RequiredArgsConstructor;

/**
 * /articles
 * /articles/{article-id}
 * /articles/search
 * /articles/search-hashtag
 */
@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;

    @GetMapping("")
    public String articles(
            @RequestParam(required = false) final SearchType searchType,
            @RequestParam(required = false) final String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Direction.DESC) final Pageable pageable,
            final ModelMap map
    ) {
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable)
                .map(ArticleResponse::from);
        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumbers(
                pageable.getPageNumber(),
                articles.getTotalPages()
        );

        map.addAttribute("articles", articles);
        map.addAttribute("paginationBarNumbers", paginationBarNumbers);
        map.addAttribute("searchTypes", SearchType.values());

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable final Long articleId, final ModelMap map) {
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(
                articleService.getArticleWithComments(articleId));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentResponse());
        map.addAttribute("totalCount", articleService.getArticleCount());

        return "articles/detail";
    }

    @GetMapping("/search-hashtag")
    public String searchArticleHashtag(
            @RequestParam(required = false) final String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Direction.DESC) final Pageable pageable,
            final ModelMap map
    ) {
        Page<ArticleResponse> articles = articleService.searchArticlesViaHashtag(searchValue, pageable)
                .map(ArticleResponse::from);
        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumbers(
                pageable.getPageNumber(),
                articles.getTotalPages()
        );
        List<String> hashtags = articleService.getHashtags();

        map.addAttribute("articles", articles);
        map.addAttribute("hashtags", hashtags);
        map.addAttribute("paginationBarNumbers", paginationBarNumbers);
        map.addAttribute("searchType", SearchType.HASHTAG);

        return "articles/search-hashtag";
    }

    @GetMapping("/form")
    public String articleForm(ModelMap map) {
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "articles/form";
    }

    @PostMapping("/form")
    public String postNewArticle(ArticleRequest articleRequest) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.saveArticle(articleRequest.toDto(UserAccountDto.of(
                "uno", "asdf1234", "uno@mail.com", "Uno", "memo", null, null, null, null
        )));

        return "redirect:/articles";
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map) {
        ArticleResponse article = ArticleResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", article);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "articles/form";
    }

    @PostMapping("/{articleId}/form")
    public String updateArticle(@PathVariable Long articleId, ArticleRequest articleRequest) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.updateArticle(articleId, articleRequest.toDto(UserAccountDto.of(
                "uno", "asdf1234", "uno@mail.com", "Uno", "memo", null, null, null, null
        )));

        return "redirect:/articles/" + articleId;
    }

    @PostMapping("/{articleId}/delete")
    public String deleteArticle(@PathVariable Long articleId) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.deleteArticle(articleId);

        return "redirect:/articles";
    }

}
