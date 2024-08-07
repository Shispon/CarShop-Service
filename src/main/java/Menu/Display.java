package Menu;

import Models.LogsModel;
import Models.OrderModel;
import Models.UserModel;
import Service.OrderService;
import Service.UserService;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Display {
    private Scanner scanner;
    private MainMenu mainMenu;
    private UserService userService;
    private OrderService orderService;
    private LogsModel logsModel;

    public Display ( MainMenu mainMenu, UserService userService,OrderService orderService) {
        this.scanner = new Scanner(System.in);
        this.mainMenu = mainMenu;
        this.userService = userService;
        this.orderService = orderService;
    }

    private void displayCarFunctions (UserModel user) {
        while(true) {
            System.out.println("1. View CarList");
            System.out.println("2. Returned");
            if ("admin".equals(user.getRole()) ||  "manager".equals(user.getRole()))
                System.out.println("3. Add NewCar");

            if("admin".equals(user.getRole()))
                System.out.println("4. Edit Car");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    mainMenu.viewCars();
                    break;
                case 2:
                    displayFunctions(user);
                    break;
                case 3:
                    if ("admin".equals(user.getRole()) ||  "manager".equals(user.getRole())) {
                        mainMenu.addCar();
                       logsModel.setLogsList(user.toString() + " " + "Create New Car" + LocalDateTime.now().toString());
                    }

                    break;
                case 4:
                    if("admin".equals(user.getRole())) {
                        mainMenu.editCar();
                        logsModel.setLogsList(user.toString() + " " + "Edit  Car" + LocalDateTime.now().toString());
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private OrderModel FindOrder(UserModel user) {
        while(true) {
            System.out.println("1. Find By ClientId");
            System.out.println("2. Find By Status");
            System.out.println("3. Find By Date");
            System.out.println("4. Find By CarId");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    mainMenu.FindOrderByUserId();
                    break;
                case 2:
                    mainMenu.FindOrderByStatus();
                    break;
                case 3:
                    mainMenu.FindOrderByDate();
                    break;
                case 4:
                    mainMenu.FindOrderByCarId();
                    break;
                case 5:
                    displayOrderFunctions(user);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }
    private void displayOrderFunctions (UserModel user) {
        while(true) {
            if ("admin".equals(user.getRole()) ||  "manager".equals(user.getRole())) {
                System.out.println("1. View Order");
                System.out.println("2. Create Order");
                System.out.println("3. Edit Order");
                System.out.println("4. Delete Order");
                System.out.println("5. Find Order");
                System.out.println("6. Returned");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        mainMenu.viewOrders(user);
                        break;
                    case 2:
                        mainMenu.createOrder(user);
                        logsModel.setLogsList(user.toString() + " " + "Create New Order" + LocalDateTime.now().toString());
                        break;
                    case 3:
                        mainMenu.editOrder();
                        logsModel.setLogsList(user.toString() + " " + "Edit Order" + LocalDateTime.now().toString());
                        break;
                    case 4:
                        mainMenu.DeleteOrder(user);
                        logsModel.setLogsList(user.toString() + " " + "Delete Order" + LocalDateTime.now().toString());
                        break;
                    case 5:
                        FindOrder(user);
                        break;
                    case 6:
                        displayFunctions(user);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }


    }

    private void displayServiceRequestFunctions(UserModel user) {
        while (true) {
            System.out.println("1. Create Service Request");
            if ("admin".equals(user.getRole()) || "manager".equals(user.getRole()))
                System.out.println("2. View Service Reauest");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    mainMenu.createServiceRequest(user);
                    logsModel.setLogsList(user.toString() + " " + "Create Service Request" + LocalDateTime.now().toString());
                    break;
                case 2:
                    if ("admin".equals(user.getRole()) || "manager".equals(user.getRole()))
                        mainMenu.viewServiceRequests(user);
                case 3:
                    displayFunctions(user);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayUsersSortedBy(UserModel user) {
        while (true) {
            System.out.println("1.Filter By User Name");
            System.out.println("2 Sorted By User Name");
            System.out.println("3. Sorted By User Role");
            System.out.println("4. Returned");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    try {
                        System.out.println("Enter User Name");
                        String userName = scanner.nextLine();
                        if (userName.trim().isEmpty()) {
                            throw new IllegalArgumentException("Username cannot be empty.");
                        }
                        if (!Pattern.matches("[a-zA-Z0-9_]+", userName)) {
                            throw new IllegalArgumentException("Username contains invalid characters. Only letters, digits, and underscores are allowed.");
                        }
                        System.out.println(userService.filterByUsername(userName));
                    }catch(IllegalArgumentException e) {
                        System.out.println("Input error: " + e.getMessage());
                        break;
                    }
                    break;
                case 2:
                        System.out.println(userService.sortByUsername());
                    break;
                case 3:
                        System.out.println(userService.sortByRole());
                    break;
                case 4:
                    displayUsersFunctions(user);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private void displayUsersFunctions (UserModel user) {
        while (true) {
            if ("admin".equals(user.getRole()) || "manager".equals(user.getRole())) {
                System.out.println("1. View Users");
                System.out.println("2. Sorted Users");
                System.out.println("3. Edit User");
                System.out.println("4. Returned");
            }

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    mainMenu.viewUsers();
                    break;
                case 2:
                    displayUsersSortedBy(user);
                    break;
                case 3:
                    mainMenu.editUser();
                    logsModel.setLogsList(user.toString() + " " + "Edit User" + LocalDateTime.now().toString());
                case 4:
                    displayFunctions(user);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public void displayFunctions (UserModel user) {
        while(true) {
            System.out.println("1. Car");
            System.out.println("2. Order");
            System.out.println("3. Service Requset");
            System.out.println("4. Returned");
            if ("admin".equals(user.getRole()) ||  "manager".equals(user.getRole())) {
                System.out.println("5. Users");
            } else if ("admin".equals(user.getRole())) {
                System.out.println("6. Out logs to File");
            }

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    displayCarFunctions(user);
                    break;
                case 2:
                    displayOrderFunctions(user);
                    break;
                case 3:
                    displayServiceRequestFunctions(user);
                case 4:
                    displayInitialMenu();
                    break;
                case 5:
                    displayUsersFunctions(user);
                    break;
                case 6:
                    logsModel.inputFile();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public void displayInitialMenu() {
        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    mainMenu.register();

                    break;
                case 2:
                    try {
                        System.out.println("Enter User Name");
                        String userName = scanner.nextLine();
                        if (userName.trim().isEmpty()) {
                            throw new IllegalArgumentException("Username cannot be empty.");
                        }
                        if (!Pattern.matches("[a-zA-Z0-9_]+", userName)) {
                            throw new IllegalArgumentException("Username contains invalid characters. Only letters, digits, and underscores are allowed.");
                        }
                        System.out.println("Enter password");
                        String password = scanner.nextLine();
                        if (password.trim().isEmpty()) {
                            throw new IllegalArgumentException("Username cannot be empty.");
                        }
                        if (!Pattern.matches("[a-zA-Z0-9_]+", password)) {
                            throw new IllegalArgumentException("Username contains invalid characters. Only letters, digits, and underscores are allowed.");
                        }
                        UserModel user = userService.authenticateUser(userName,password);
                        if (user != null) {
                            System.out.println("Login successful.");
                            displayFunctions(user);
                        } else {
                            System.out.println("Invalid username or password.");
                        }
                    }catch(IllegalArgumentException e) {
                        System.out.println("Input error: " + e.getMessage());
                        break;
                    }
                case 3:
                    return;
            }
        }
    }
}
