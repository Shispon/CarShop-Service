package service;

import models.AuditModel;
import repository.AuditRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AuditService {
    private final AuditRepository auditRepository;
    private Integer id;
    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
        id = 0;
    }
    public void addAction(String action, String username) {
        AuditModel newAudit = AuditModel.builder()
                .id(id++)
                .action(action)
                .username(username)
                .build();
        auditRepository.create(newAudit);
    }
    public List<AuditModel> getAllLogs() {
        return auditRepository.findAll();
    }
    public void exportAudit(String filename) {
        List<AuditModel> logs = getAllLogs();
        if (logs.isEmpty()) {
            System.out.println("Нет записей для экспорта.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (AuditModel log : logs) {

                writer.write(formatAudit(log));
                writer.newLine();
            }
            System.out.println("Аудит успешно экспортирован в " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при экспорте аудита: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private String formatAudit(AuditModel log) {
        return String.format("%s,%s,%s,%s", log.getId(), log.getUsername(), log.getAction(), log.getTimestamp());
    }
}
