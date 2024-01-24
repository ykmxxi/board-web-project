package com.example.board.domain.constant;

import lombok.Getter;

public enum FormStatus {

    CREATE("저장", false),
    UPDATE("수정", true);

    @Getter private final String description;
    @Getter private final Boolean update;

    FormStatus(final String description, final Boolean update) {
        this.description = description;
        this.update = update;
    }

}
