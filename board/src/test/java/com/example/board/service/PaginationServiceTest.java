package com.example.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayName("비즈니스 로직 : 페이지네이션")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = PaginationService.class)
class PaginationServiceTest {

    private final PaginationService paginationService;

    public PaginationServiceTest(@Autowired final PaginationService paginationService) {
        this.paginationService = paginationService;
    }

    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 반환")
    @ParameterizedTest(name = "현재 페이지:{0}, 총 페이지:{1} => {2}")
    @MethodSource("dataForGetPaginationBarNumbers")
    void 페이징_바_리스트(int currentPage, int totalPages, List<Integer> expected) {
        // when
        List<Integer> result = paginationService.getPaginationBarNumbers(currentPage, totalPages);

        // then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> dataForGetPaginationBarNumbers() {
        return Stream.of(
                of(0, 13, List.of(0, 1, 2, 3, 4)),
                of(1, 13, List.of(0, 1, 2, 3, 4)),
                of(2, 13, List.of(0, 1, 2, 3, 4)),
                of(3, 13, List.of(1, 2, 3, 4, 5)),
                of(10, 13, List.of(8, 9, 10, 11, 12)),
                of(11, 13, List.of(9, 10, 11, 12)),
                of(12, 13, List.of(10, 11, 12))
        );
    }

    @DisplayName("현재 설정되어 있는 페이지네이션 바의 길이를 반환")
    @Test
    void 페이지네이션_바_길이() {
        // when & then
        assertThat(paginationService.currentBarLength()).isEqualTo(5);
    }

}
