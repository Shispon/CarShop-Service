package menu;

import models.CarModel;
import models.UserModel;
import service.AuditService;
import service.CarService;
import service.UserService;

import java.util.List;
import java.util.Scanner;

public class CarDisplay {
    private final CarService carService;
    private final AuditService auditService;
    private final UserService userService;


    public CarDisplay(CarService carService, AuditService auditService, UserService userService) {
        this.carService = carService;
        this.auditService = auditService;
        this.userService = userService;
    }

    public void manageCars(Scanner scanner) {
        System.out.println("Меню автомобилей");
        System.out.println("1. Добавить автомобиль");
        System.out.println("2. Редактировать автомобиль");
        System.out.println("3. Удалить автомобиль");
        System.out.println("4. Просмотреть все автомобили");
        System.out.println("5. Меню поиска");
        System.out.print("Выберите действие: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                addCar(scanner);
                break;
            case "2":
                updateCar(scanner);
                break;
            case "3":
                deleteCar(scanner);
                break;
            case "4":
                viewAllCars();
                break;
            case "5":
                findAllCars(scanner);
                break;
            default:
                System.out.println("Введите число из предложенных");
        }
    }
    private void findAllCars(Scanner scanner){
        System.out.println("Меню поиска автомобилей");
        System.out.println("1. По марке");
        System.out.println("2. По году выпуска");
        System.out.println("3. По модели");
        System.out.println("4. По цене");
        System.out.println("5. По состоянию");
        System.out.print("Выберите действие: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                viewAllCarsByBrand(scanner);
                break;
            case "2":
                viewAllCarsByYear(scanner);
                break;
            case "3":
                viewAllCarsByModel(scanner);
                break;
            case "4":
                viewAllCarsByPrice(scanner);
                break;
            case "5":
                viewAllCarsByCondition(scanner);
                break;
            default:
                System.out.println("Введите число из предложенных");
        }


    }
    private void addCar(Scanner scanner) {
        System.out.print("Марка: ");
        String brand = scanner.nextLine();
        System.out.print("Модель: ");
        String model = scanner.nextLine();
        System.out.print("Год выпуска: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Цена: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Состояние: ");
        String condition = scanner.nextLine();

        CarModel car = new CarModel(brand, model, year, price, condition);
        carService.addCar(car);

        UserModel currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            auditService.addAction("Добавление автомобиля", currentUser.getUsername(), currentUser.getId());
            System.out.println("Автомобиль добавлен и запись в аудит создана.");
        } else {
            System.out.println("Автомобиль добавлен, но запись в аудит не создана, так как пользователь не авторизован.");
        }
    }

    private void updateCar(Scanner scanner) {
        System.out.print("ID автомобиля: ");
        int carIdToUpdate = Integer.parseInt(scanner.nextLine());

        // Получаем существующий объект CarModel по ID
        CarModel existingCar = carService.getCarById(carIdToUpdate);

        if (existingCar != null) {
            // Обновляем информацию о машине
            System.out.print("Новая марка: ");
            String newBrand = scanner.nextLine();
            System.out.print("Новая модель: ");
            String newModel = scanner.nextLine();
            System.out.print("Новый год выпуска: ");
            int newYear = Integer.parseInt(scanner.nextLine());
            System.out.print("Новая цена: ");
            double newPrice = Double.parseDouble(scanner.nextLine());
            System.out.print("Новое состояние: ");
            String newCondition = scanner.nextLine();

            // Создаем новый объект CarModel с обновленными значениями
            CarModel updatedCar = existingCar.toBuilder()
                    .brand(newBrand)
                    .model(newModel)
                    .year(newYear)
                    .price(newPrice)
                    .condition(newCondition)
                    .build();
            UserModel currentUser = userService.getCurrentUser();

            // Обновляем автомобиль через сервис
            if (carService.updateCar(updatedCar)) {
                // Добавляем запись в аудит
                auditService.addAction("Редактирование автомобиля", "admin",currentUser.getId());
                System.out.println("Автомобиль обновлен.");
            } else {
                System.out.println("Ошибка при обновлении автомобиля.");
            }
        } else {
            System.out.println("Автомобиль не найден.");
        }
    }

    private void deleteCar(Scanner scanner) {
        System.out.print("ID автомобиля: ");
        int carIdToDelete = Integer.parseInt(scanner.nextLine());
        UserModel currentUser = userService.getCurrentUser();
        if (carService.deleteCar(carIdToDelete)) {
            auditService.addAction("Удаление автомобиля", "admin", currentUser.getId());
            System.out.println("Автомобиль удален.");
        } else {
            System.out.println("Автомобиль не найден.");
        }
    }

    private void viewAllCars() {
        List<CarModel> cars = carService.getAllCars();
        for (CarModel car : cars) {
            System.out.println(car);
        }
    }
    private void viewAllCarsByBrand(Scanner scanner) {
        System.out.print("Введите марку: ");
        String brand =scanner.nextLine();
        List<CarModel> cars = carService.searchCarsByBrand(brand);
        for (CarModel car : cars) {
            System.out.println(car);
        }
    }
    private void viewAllCarsByYear(Scanner scanner) {
        System.out.print("Введите год: ");
        Integer year =Integer.valueOf(scanner.nextLine());
        List<CarModel> cars = carService.searchCarsByYear(year);
        for (CarModel car : cars) {
            System.out.println(car);
        }
    }
    private void viewAllCarsByModel(Scanner scanner) {
        System.out.print("Введите модель: ");
        String model =scanner.nextLine();
        List<CarModel> cars = carService.searchCarsByModel(model);
        for (CarModel car : cars) {
            System.out.println(car);
        }
    }
    private void viewAllCarsByCondition(Scanner scanner) {
        System.out.print("Введите состояние: ");

        String conditions =scanner.nextLine();
        List<CarModel> cars = carService.searchCarsByCondition(conditions);
        for (CarModel car : cars) {
            System.out.println(car);
        }
    }
    private void viewAllCarsByPrice(Scanner scanner) {
        System.out.print("Введите верхнюю границу: ");
        double max = Double.parseDouble(scanner.nextLine());
        System.out.print("Введите нижнюю границу : ");
        double min = Double.parseDouble(scanner.nextLine());
        List<CarModel> cars = carService.searchCarsByPrice(min,max);
        for (CarModel car : cars) {
            System.out.println(car);
        }
    }
}
