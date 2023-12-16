package com.example.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
