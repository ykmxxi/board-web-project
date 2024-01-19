package com.example.board.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.board.config.JpaConfig;
import com.example.board.domain.Article;
import com.example.board.domain.UserAccount;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    JpaRepositoryTest(@Autowired final ArticleRepository articleRepository,
                      @Autowired final ArticleCommentRepository articleCommentRepository,
                      @Autowired final UserAccountRepository userAccountRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void 기본_select_테스트() {
        // given

        // when
        List<Article> articles = articleRepository.findAll();

        // then
        assertThat(articles).isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert 테스트")
    @Test
    void 기본_insert_테스트() {
        // given
        long previous = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(
                UserAccount.of("ykmxxi1", "password", "email@email.com", "ykmxxi", "hi")
        );

        // when
        Article savedArticle = articleRepository.save(Article.of(userAccount, "new article", "new content", "new"));

        // then
        assertThat(articleRepository.count()).isEqualTo(previous + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void 기본_update_테스트() {
        // given
        Article article = articleRepository.findById(1L)
                .orElseThrow();
        String updated = "#newHashtag";

        // when
        article.setHashtag(updated);
        Article savedArticle = articleRepository.save(article);

        // then
        assertThat(article).extracting("hashtag").isEqualTo(updated);
    }

    @DisplayName("delete 테스트")
    @Test
    void 기본_delete_테스트() {
        // given
        Article article = articleRepository.findById(1L)
                .orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();

        // when
        articleRepository.delete(article);

        // then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
    }

}
