package menu;

import models.RequestModel;
import models.ServiceEnum;
import models.StatusEnum;
import models.UserModel;
import service.AuditService;
import service.RequestService;
import service.UserService;

import java.util.List;
import java.util.Scanner;

public class RequestDisplay {
    private final RequestService requestService;
    private final AuditService auditService;
    private final UserService userService;


    public RequestDisplay(RequestService orderService, AuditService auditService, UserService userService) {
        this.requestService = orderService;
        this.auditService = auditService;
        this.userService = userService;
    }

    public void manageServiceRequests(Scanner scanner) {
        System.out.println("Меню заказами на обслуживание");
        System.out.println("1. Создать заказ");
        System.out.println("2. Редактировать заказ");
        System.out.println("3. Удалить заказ");
        System.out.println("4. Просмотреть все заказы");
        System.out.print("Выберите действие: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                createServiceRequest(scanner);
                break;
            case "2":
                updateServiceRequest(scanner);
                break;
            case "3":
                deleteServiceRequest(scanner);
                break;
            case "4":
                viewAllServiceRequests();
                break;
            default:
                System.out.println("Введите число из предложенных");
        }
    }

    private void createServiceRequest(Scanner scanner) {
        System.out.print("Марка автомобиля: ");
        String carBrand = scanner.nextLine();
        System.out.print("Модель автомобиля: ");
        String carModel = scanner.nextLine();
        System.out.print("Имя клиента: ");
        String username = scanner.nextLine();
        System.out.print("Тип обслуживания: ");
        String serviceType = scanner.nextLine();
        System.out.print("Статус заказа: ");
        String status = scanner.nextLine();
        RequestModel request =new RequestModel(carBrand,carModel,username, ServiceEnum.valueOf(serviceType), StatusEnum.valueOf(status));
        requestService.addRequest(request);
        UserModel currentUser = userService.getCurrentUser();
        auditService.addAction("Создание заказа на обслуживание", "admin",currentUser.getId());
        System.out.println("Заказ на обслуживание создан.");
    }

    private void updateServiceRequest(Scanner scanner) {
        System.out.print("ID заказа: ");
        int administrativeOrderId = Integer.parseInt(scanner.nextLine());

        // Получаем существующий объект RequestModel по ID
        RequestModel existingRequest = requestService.getOrderById(administrativeOrderId);

        if (existingRequest != null) {
            // Обновляем информацию о запросе
            System.out.print("Новая марка автомобиля: ");
            String newCarBrand = scanner.nextLine();
            System.out.print("Новая модель автомобиля: ");
            String newCarModel = scanner.nextLine();
            System.out.print("Новое имя клиента: ");
            String newUsername = scanner.nextLine();
            System.out.print("Новый тип обслуживания: ");
            ServiceEnum newServiceType = ServiceEnum.valueOf(scanner.nextLine());
            System.out.print("Новый статус заказа: ");
            StatusEnum newStatus = StatusEnum.valueOf(scanner.nextLine());

            // Создаем новый объект RequestModel с обновленными значениями
            RequestModel updatedRequest = existingRequest.toBuilder()
                    .carBrand(newCarBrand)
                    .carModel(newCarModel)
                    .username(newUsername)
                    .serviceType(newServiceType)
                    .status(newStatus)
                    .build();

            UserModel currentUser = userService.getCurrentUser();
            if (requestService.updateRequest(updatedRequest)) {
                // Добавляем запись в аудит
                auditService.addAction("Редактирование заказа на обслуживание", "admin",currentUser.getId());
                System.out.println("Заказ на обслуживание обновлен.");
            } else {
                System.out.println("Ошибка при обновлении заказа на обслуживание.");
            }
        } else {
            System.out.println("Заказ на обслуживание не найден.");
        }
    }

    private void deleteServiceRequest(Scanner scanner) {
        System.out.print("ID заказ: ");
        Integer id = Integer.parseInt(scanner.nextLine());
        UserModel currentUser = userService.getCurrentUser();
        if (requestService.deleteRequestById(id)) {
            auditService.addAction("Удаление заказа на обслуживание", "admin",currentUser.getId());
            System.out.println("Заказ на обслуживание удален.");
        } else {
            System.out.println("Заказ на обслуживание не найден.");
        }
    }

    private void viewAllServiceRequests() {
        List<RequestModel> administrativeOrders = requestService.getAllRequest();
        for (RequestModel administrativeOrder : administrativeOrders) {
            System.out.println(administrativeOrder);
        }
    }
}
