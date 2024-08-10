package repository;

import models.AuditModel;

import java.util.ArrayList;
import java.util.List;

public class AuditRepository implements CrudRepository<AuditModel,Integer> {
    private final List<AuditModel> auditList;
    private Integer id = 0;

    public AuditRepository() {
        auditList = new ArrayList<>();
    }

    @Override
    public AuditModel create(AuditModel audit) {
        AuditModel newAudit = audit.toBuilder()
                .id(++id)
                .build();
        auditList.add(newAudit);
        return newAudit;
    }

    @Override
    public AuditModel read(Integer id) {
        return auditList.stream()
                .filter(audit -> audit.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public AuditModel update(AuditModel audit) {
        AuditModel existingAudit = read(audit.getId());
        if (existingAudit != null) {
            AuditModel updateAudit = existingAudit.toBuilder()
                    .username(audit.getUsername())
                    .action(audit.getAction())
                    .timestamp(audit.getTimestamp())
                    .build();
            auditList.set(auditList.indexOf(existingAudit), updateAudit);
            return updateAudit;
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return auditList.removeIf(audit -> audit.getId().equals(id));
    }

    @Override
    public List<AuditModel> findAll() {
        return auditList;
    }

}
