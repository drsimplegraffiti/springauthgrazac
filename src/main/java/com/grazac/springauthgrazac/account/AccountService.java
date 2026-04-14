package com.grazac.springauthgrazac.account;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(AccountCreateRequest request) {
        Account account = Account.builder()
                .accountNumber(request.getAccountNumber())
                .hasAccount(false)
                .userId(request.getUserId())
                .accountName(request.getAccountName()).build();
        accountRepository.save(account);
    }

    private String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        return String.format("%0" + 10 + "d", random.nextInt((int) Math.pow(10, 10)));
    }

    @PostConstruct
    private void init() {
        if (accountRepository.count() > 1) {
            System.out.println("already seeded");
        } else {
            for (int i = 0; i < 5; i++) {
                var name = "User-";
                Account account = Account.builder()
                        .accountNumber(generateAccountNumber())
                        .hasAccount(false)
                        .userId(Long.valueOf(i + 1))
                        .accountName(name + i).build();
                accountRepository.save(account);
            }
        }
    }
}
