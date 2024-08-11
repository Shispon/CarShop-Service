package menu;

import models.OrderModel;
import models.StatusEnum;
import models.UserModel;
import service.AuditService;
import service.OrderService;
import service.UserService;

import java.util.List;
import java.util.Scanner;

public class OrderDisplay {
    private final OrderService orderService;
    private final AuditService auditService;
    private final UserService userService;


    public OrderDisplay(OrderService orderService, AuditService auditService, UserService userService) {
        this.orderService = orderService;
        this.auditService = auditService;
        this.userService = userService;
    }

    public void manageOrders(Scanner scanner) {
        System.out.println("Меню заказов на покупку ");
        System.out.println("1. Создать заказ");
        System.out.println("2. Редактировать заказ");
        System.out.println("3. Удалить заказ");
        System.out.println("4. Просмотреть все заказы");
        System.out.print("Выберите действие: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                createOrder(scanner);
                break;
            case "2":
                updateOrder(scanner);
                break;
            case "3":
                deleteOrder(scanner);
                break;
            case "4":
                viewAllOrders();
                break;
            default:
                System.out.println("Введите число из предложенных");
        }
    }

    private void createOrder(Scanner scanner) {
        System.out.print("ID автомобиля: ");
        Integer carId = Integer.valueOf(scanner.nextLine());
        System.out.print("ID клиента: ");
        Integer userID = Integer.valueOf(scanner.nextLine());
        System.out.print("Статус заказа(inProcessing,\n" +
                "    started,\n" +
                "    ready: ");
        String statusS = scanner.nextLine().toUpperCase();
        StatusEnum status = StatusEnum.valueOf(statusS);
        OrderModel order = new OrderModel( carId, userID, status);
        UserModel currentUser = userService.getCurrentUser();
        if(orderService.searchOrdersByUserIdAndCarID(userID,carId).isEmpty()) {
            orderService.addOrder(order);
        }
        auditService.addAction("Создание заказа", "admin",currentUser.getId());
        System.out.println("Заказ создан.");
    }

    private void updateOrder(Scanner scanner) {
        System.out.print("ID заказа: ");
        int orderIdToUpdate = Integer.parseInt(scanner.nextLine());

        // Получаем существующий заказ
        OrderModel existingOrder = orderService.getOrderById(orderIdToUpdate);

        if (existingOrder != null) {
            System.out.print("Новый статус заказа: ");
            StatusEnum newStatus = StatusEnum.valueOf(scanner.nextLine());

            // Создаем новый объект заказа с обновленным статусом
            OrderModel updatedOrder = existingOrder.toBuilder()
                    .status(newStatus) // Обновляем статус
                    .build();

            UserModel currentUser = userService.getCurrentUser();
            if (orderService.updateOrder(updatedOrder)) {
                // Добавляем запись в аудит
                auditService.addAction("Редактирование заказа", "admin",currentUser.getId());
                System.out.println("Заказ обновлен.");
            } else {
                System.out.println("Ошибка при обновлении заказа.");
            }
        } else {
            System.out.println("Заказ не найден.");
        }
    }

    private void deleteOrder(Scanner scanner) {
        System.out.print("ID заказа: ");
        int orderIdToDelete = Integer.parseInt(scanner.nextLine());
        UserModel currentUser = userService.getCurrentUser();
        if (orderService.deleteOrder(orderIdToDelete)) {
            auditService.addAction("Удаление заказа", "admin",currentUser.getId());
            System.out.println("Заказ удален.");
        } else {
            System.out.println("Заказ не найден.");
        }
    }

    private void viewAllOrders() {
        List<OrderModel> orders = orderService.getAllOrders();
        for (OrderModel order : orders) {
            System.out.println(order);
        }
    }
}
