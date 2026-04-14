package com.grazac.springauthgrazac.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "select * from account where has_account = false", nativeQuery = true)
    List<Account> findAllAccounts();
}
