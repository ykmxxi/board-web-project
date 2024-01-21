package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.board.dto.UserAccountDto;
import com.example.board.dto.request.ArticleCommentRequest;
import com.example.board.service.ArticleCommentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewArticleComment(final ArticleCommentRequest articleCommentRequest) {
        // TODO: 추후 진짜 인증정보를 넣어줘야 한다
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(
                UserAccountDto.of("ykmxxi", "pw1234", "ykmxxi@mail.com", null, null)
        ));

        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable final Long commentId, final Long articleId) {
        articleCommentService.deleteArticleComment(commentId);

        return "redirect:/articles/" + articleId;
    }

}
