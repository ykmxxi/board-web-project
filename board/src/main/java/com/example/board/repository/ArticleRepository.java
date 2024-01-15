package com.example.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.board.domain.Article;
import com.example.board.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle> {

    Page<Article> findByTitle(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Article> findByHashtagContaining(String hashtag, Pageable pageable);

    @Override
    default void customize(final QuerydslBindings bindings, final QArticle root) {
        bindings.excludeUnlistedProperties(true); // 선택적으로 검색 조건을 사용
        bindings.including(
                root.title,
                root.content,
                root.hashtag,
                root.createdAt,
                root.createdBy); // 제목, 본문, 해시태그, 생성일시, 생성자 사용

        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // 제목 검색 파라미터는 1개
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // 본문 검색 파라미터는 1개
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase); // 해시태그 검색 파라미터는 1개
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 생성일시 검색 파라미터는 1개
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // 생성자 검색 파라미터는 1개
    }

}
