package com.grazac.springauthgrazac.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    void deleteByTimestampBefore(LocalDateTime oneMinuteAgo);
}
