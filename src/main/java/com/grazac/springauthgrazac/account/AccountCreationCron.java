package com.grazac.springauthgrazac.account;

import com.grazac.springauthgrazac.audit.AuditLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AccountCreationCron {


    private final AccountRepository accountRepository;

    public AccountCreationCron(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Scheduled(cron = "* * * 4 * *") // Every day at 2 AM\
    @Transactional
    public void createAccountCron() {
        System.out.println("========================================");
        System.out.println("running every one minute");
        System.out.println("========================================");
        //current time now() - 1minute
//        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1
        List<Account> allAccounts = accountRepository.findAllAccounts();

        allAccounts.stream().forEach(account -> {
            account.setHasAccount(true);
        });
    }
}
