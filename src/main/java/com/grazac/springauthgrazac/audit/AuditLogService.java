package com.grazac.springauthgrazac.audit;


import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void create(AuditRequest request){
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(request.getAction());
        auditLog.setUserId(request.getUserId());
        auditLogRepository.save(auditLog);
    }
}
