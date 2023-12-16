package com.example.board.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false)
    private String title;
    @Setter @Column(nullable = false, length = 10_000)
    private String content;
    @Setter
    private String hashtag;

    @CreatedDate @Column(nullable = false)
    private LocalDateTime createdAt;
    @CreatedBy @Column(nullable = false)
    private String createdBy;
    @LastModifiedDate @Column(nullable = false)
    private LocalDateTime modifiedAt;
    @LastModifiedBy @Column(nullable = false)
    private String modifiedBy;

    private Article(final String title, final String content, final String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(final String title, final String content, final String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article article)) {
            return false;
        }
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
