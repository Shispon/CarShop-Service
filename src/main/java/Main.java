import menu.*;
import repository.*;
import service.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService(new UserRepository());
        CarService carService = new CarService(new CarRepository());
        OrderService orderBuyService = new OrderService(new OrderRepository());
        RequestService requestService = new RequestService(new RequestRepository());
        AuditService auditService = new AuditService(new AuditRepository());

        UserDisplay userDisplay = new UserDisplay(userService);
        CarDisplay carDisplay = new CarDisplay(carService, auditService);
        OrderDisplay orderDisplay = new OrderDisplay(orderBuyService, auditService);
        RequestDisplay administrativeOrderDisplay = new RequestDisplay(requestService, auditService);
        AuditDisplay auditDisplay = new AuditDisplay(auditService);


        while (true) {
            System.out.println("Главное меню ");
            System.out.println("1. Меню пользователей");
            System.out.println("2. Меню автомобилей");
            System.out.println("3. Меню заказов на покупку");
            System.out.println("4. Меню заказов на обслуживание");
            System.out.println("5. Меню аудита");
            System.out.println("0. Выйти");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    userDisplay.manageUser(scanner);
                    break;
                case "2":
                    carDisplay.manageCars(scanner);
                    break;
                case "3":
                    orderDisplay.manageOrders(scanner);
                    break;
                case "4":
                    administrativeOrderDisplay.manageServiceRequests(scanner);
                    break;
                case "5":
                    auditDisplay.manageAudit(scanner);
                    break;
                case "0":
                    System.out.println("Выход");
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }
}