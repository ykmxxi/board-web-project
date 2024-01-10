package com.example.board.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;

/**
 * 값 타입
 */
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditingFields {

    @DateTimeFormat(iso = ISO.DATE_TIME) // 파싱(Parsing) 룰
    @CreatedDate @Column(nullable = false)
    private LocalDateTime createdAt;

    @CreatedBy @Column(nullable = false, updatable = false, length = 100)
    private String createdBy;

    @DateTimeFormat(iso = ISO.DATE_TIME) // 파싱(Parsing) 룰
    @LastModifiedDate @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @LastModifiedBy @Column(nullable = false, length = 100)
    private String modifiedBy;

}
