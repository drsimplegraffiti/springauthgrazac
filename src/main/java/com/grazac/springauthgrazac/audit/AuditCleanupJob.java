package com.grazac.springauthgrazac.audit;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Component
public class AuditCleanupJob {

    private final AuditLogRepository auditLogRepository;

    public AuditCleanupJob(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Scheduled(cron = "* * 4 * * *") // Every day at 2 AM
    public void cleanOldLogs() {
        System.out.println("========================================");
        System.out.println("running every one minute");
        System.out.println("========================================");
        //current time now() - 1minute
//        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        auditLogRepository.deleteAll();
    }
}
