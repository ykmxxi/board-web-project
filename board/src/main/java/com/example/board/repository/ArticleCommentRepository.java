package com.example.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.board.domain.ArticleComment;
import com.example.board.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    @Override
    default void customize(final QuerydslBindings bindings, final QArticleComment root) {
        bindings.excludeUnlistedProperties(true); // 선택적으로 검색 조건을 사용
        bindings.including(
                root.content,
                root.createdAt,
                root.createdBy); // 제목, 본문, 해시태그, 생성일시, 생성자 사용

        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 생성일시 검색 파라미터는 1개만
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // 생성자 검색 파라미터는 1개만
    }

}
