package com.example.board.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(final int currentPage, final int totalPages) {
        int startPage = Math.max(currentPage - (BAR_LENGTH / 2), 0); // 음수 방지
        int endPage = Math.min(startPage + BAR_LENGTH, totalPages); // 총 페이지 초과 방지

        return IntStream.range(startPage, endPage)
                .boxed()
                .toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }

}
