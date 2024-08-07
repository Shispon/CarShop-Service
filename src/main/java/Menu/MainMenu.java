package Menu;

import Models.CarModel;
import Models.OrderModel;
import Models.RequestModel;
import Models.UserModel;
import Service.CarService;
import Service.OrderService;
import Service.RequestService;
import Service.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;

public class MainMenu {
    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private RequestService requestService;
    private Scanner scanner;

    public MainMenu(UserService userService, CarService carService, OrderService orderService, RequestService requestService) {
        this.userService = userService;
        this.carService = carService;
        this.orderService = orderService;
        this.requestService = requestService;
        this.scanner = new Scanner(System.in);
    }



    public void register() {

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (admin, manager, client): ");
        String role = scanner.nextLine();

        UserModel user = new UserModel( username, password, role);
        try {
            userService.registerUser(user);
            System.out.println("User registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("User already exists.");
        }
    }



    public OrderModel FindOrderByUserId() {
        System.out.println("Enter Client Id");
        String clientId = scanner.nextLine();
        return orderService.getOrderById(clientId);
    }
    public OrderModel FindOrderByStatus() {
        System.out.println("Enter Status");
        String status = scanner.nextLine();
        return orderService.getOrderByStatus(status);
    }

    public OrderModel FindOrderByDate () {
        System.out.println("Enter Date by format yyyy-MM-dd (2024-08-05)");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = scanner.nextLine();
        try {
            Date date = dateFormat.parse(dateString);
            return orderService.getOrderByDate(date);
        } catch (ParseException e) {
            System.out.println("Exception: Invalid date format. Make sure the date matches the format yyyy-MM-dd.");
        }
        return null;
    }

    public OrderModel FindOrderByCarId(){
        System.out.println("Enter Car Id");
        String carId = scanner.nextLine();
        return orderService.getOrderByCarId(carId);
    }

    public void viewCars() {
        carService.getAllCars().forEach(car -> System.out.println(car));
    }
    public void viewUsers() {
        userService.getAllUsers().forEach(user-> System.out.println(user));
    }

    public void createOrder(UserModel user) {
        System.out.print("Enter car ID: ");
        String carId = scanner.nextLine();
        CarModel car = carService.getCar(carId);
        if (car != null) {
            if (!orderService.isCarOrdered(carId)) {
                OrderModel order = new OrderModel( carId, user.getId());
                orderService.addOrder(order);
                System.out.println("Order created successfully.");
            } else {
                System.out.println("Car is already ordered.");
            }
        } else {
            System.out.println("Car not found.");
        }
    }

    public void DeleteOrder (UserModel user) {
        if ("admin".equals(user.getRole()) || "manager".equals(user.getRole()))
            orderService.deleteOrder(user.getId());
    }

    public void viewOrders(UserModel user) {
        List<OrderModel> orders;
        if ("admin".equals(user.getRole()) || "manager".equals(user.getRole())) {
            orders = orderService.getAllOrders().stream().collect(Collectors.toList());
        } else {
            orders = orderService.searchOrders(user.getId(), null);
        }
        orders.forEach(order -> System.out.println(order));
    }

    public void createServiceRequest(UserModel user) {
        System.out.print("Enter car ID: ");
        String carId = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        RequestModel request = new RequestModel(carId, user.getId(), description, "new");
        requestService.addRequest(request);
        System.out.println("Service request created successfully.");
    }

    public void viewServiceRequests(UserModel user) {
        List<RequestModel> requests;
        if ("admin".equals(user.getRole()) || "manager".equals(user.getRole())) {
            requests = requestService.getAllRequests().stream().collect(Collectors.toList());
        } else {
            requests = requestService.searchRequests(user.getId(), null);
        }
        requests.forEach(request -> System.out.println(request));
    }

    public void addCar() {
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter model: ");
        String model = scanner.nextLine();
        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        System.out.print("Enter condition: ");
        String condition = scanner.nextLine();

        CarModel car = new CarModel( brand, model, year, price, condition);
        carService.addCar(car);
        System.out.println("Car added successfully.");
    }

    public void editUser() {
        System.out.print("Enter user ID to edit: ");
        String userId = scanner.nextLine();
        UserModel user = userService.getUser(userId);
        if (user != null) {
            System.out.print("Enter new UserName (leave empty to keep current): ");
            String userName = scanner.nextLine();
            if (!userName.isEmpty()) {
                user.setUsername(userName);
            }

            System.out.print("Enter new Password (leave empty to keep current): ");
            String password = scanner.nextLine();
            if (!password.isEmpty()) {
                user.setPassword(password);
            }

            System.out.print("Enter new role (leave empty to keep current): ");
            String role = scanner.nextLine();
            if (!role.isEmpty()) {
                user.setRole(role);
            }

            System.out.println("User updated successfully.");
        } else {
            System.out.println("User not found.");
        }
    }
    public void editCar() {
        System.out.print("Enter car ID to edit: ");
        String carId = scanner.nextLine();
        CarModel car = carService.getCar(carId);
        if (car != null) {
            System.out.print("Enter new brand (leave empty to keep current): ");
            String brand = scanner.nextLine();
            if (!brand.isEmpty()) {
                car.setBrand(brand);
            }

            System.out.print("Enter new model (leave empty to keep current): ");
            String model = scanner.nextLine();
            if (!model.isEmpty()) {
                car.setModel(model);
            }

            System.out.print("Enter new year (leave empty to keep current): ");
            String yearStr = scanner.nextLine();
            if (!yearStr.isEmpty()) {
                car.setYear(Integer.parseInt(yearStr));
            }

            System.out.print("Enter new price (leave empty to keep current): ");
            String priceStr = scanner.nextLine();
            if (!priceStr.isEmpty()) {
                car.setPrice(Double.parseDouble(priceStr));
            }

            System.out.print("Enter new condition (leave empty to keep current): ");
            String condition = scanner.nextLine();
            if (!condition.isEmpty()) {
                car.setCondition(condition);
            }

            carService.updateCar(car);
            System.out.println("Car updated successfully.");
        } else {
            System.out.println("Car not found.");
        }
    }

    public void deleteCar() {
        System.out.print("Enter car ID to delete: ");
        String carId = scanner.nextLine();
        carService.deleteCar(carId);
        System.out.println("Car deleted successfully.");
    }

    public void editOrder() {
        System.out.print("Enter order ID to edit: ");
        String orderId = scanner.nextLine();
        OrderModel order = orderService.getOrderById(orderId);
        if (order != null) {
            System.out.print("Enter new status (leave empty to keep current): ");
            String status = scanner.nextLine();
            if (!status.isEmpty()) {
                order.setStatus(status);
            }

            orderService.updateOrder(order);
            System.out.println("Order updated successfully.");
        } else {
            System.out.println("Order not found.");
        }
    }
}
