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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.board.domain.type.SearchType;
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

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable final Long articleId, final ModelMap map) {
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentResponse());
        map.addAttribute("totalCount", articleService.getArticleCount());

        return "articles/detail";
    }

}
