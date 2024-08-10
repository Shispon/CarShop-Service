package menu;

import models.RoleEnum;
import models.UserModel;
import service.UserService;

import java.util.Scanner;

public class UserDisplay {
    private final UserService userService;


    public UserDisplay(UserService userService) {
        this.userService = userService;

    }

    public void manageUser(Scanner scanner) {
        System.out.println("Меню пользователей");
        System.out.println("1. Регистрация");
        System.out.println("2. Авторизация");
        System.out.print("Выберите действие: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                registerUser(scanner);
                break;
            case "2":
                loginUser(scanner);
                break;
            default:
                System.out.println("Введите число из предложенных");
        }
    }

    public void registerUser(Scanner scanner) {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        System.out.print("Введите роль (ADMIN,MANAGER,CLIENT): ");
        String roleS = scanner.nextLine();
        RoleEnum role = RoleEnum.valueOf(roleS);
        UserModel user = new UserModel(username, password, role);
        userService.registerUser(user);
        System.out.println("Пользователь успешно зарегистрирован.");
    }

    public void loginUser(Scanner scanner) {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        if (userService.authenticateUser(username, password)) {
            System.out.println("Успешная авторизация.");
        } else {
            System.out.println("Неверное имя пользователя или пароль.");
        }
    }
}
