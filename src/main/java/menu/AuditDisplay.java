package menu;

import models.AuditModel;
import service.AuditService;

import java.util.List;
import java.util.Scanner;

public class AuditDisplay {
    private final AuditService auditService;

    public AuditDisplay(AuditService auditService) {
        this.auditService = auditService;
    }
    public void manageAudit(Scanner scanner){
        System.out.println("Меню пользователей");
        System.out.println("1. Посмотреть аудит");
        System.out.println("2. Вывести в файл");
        System.out.print("Выберите действие: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                viewAuditLogs();
                break;
            case "2":
                writeToFile(scanner);
                break;
            default:
                System.out.println("Введите число из предложенных");
        }}

    public void viewAuditLogs() {
        List<AuditModel> logs = auditService.getAllLogs();
        for (AuditModel log : logs) {
            System.out.println(log);
        }
    }
    public void writeToFile(Scanner scanner) {
        System.out.println("Введите имя файла");
        String nameFile = scanner.nextLine();
        auditService.exportAudit(nameFile);
    }
}
