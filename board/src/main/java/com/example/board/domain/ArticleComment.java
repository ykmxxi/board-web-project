package com.example.board.domain;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
public class ArticleComment extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Exclude
    private Article article;
    @Setter @Column(nullable = false, length = 500)
    private String content;

    private ArticleComment(final Article article, final String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(final Article article, final String content) {
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleComment articleComment)) {
            return false;
        }
        return id != null && id.equals(articleComment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
