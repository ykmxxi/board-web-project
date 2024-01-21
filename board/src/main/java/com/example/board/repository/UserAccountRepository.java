package com.example.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board.domain.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

}
