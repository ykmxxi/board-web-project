package com.example.board.config;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import com.example.board.domain.UserAccount;
import com.example.board.repository.UserAccountRepository;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountRepository userAccountRepository;

    @BeforeTestMethod
    public void securitySetUp() {
        given(userAccountRepository.findById(anyString()))
                .willReturn(Optional.of(UserAccount.of(
                        "ykmxxiTest",
                        "pw",
                        "ykmxxi-test@email.com",
                        "ykmxxi-test",
                        "test memo"
                )));
    }

}
