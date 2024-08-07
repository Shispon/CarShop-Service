package Service;

import Models.CarModel;

import java.util.*;
import java.util.stream.Collectors;

public class CarService {
    private Map<String, CarModel> cars = new HashMap<>();

    public void addCar(CarModel car) {
        cars.put(car.getId(), car);
    }

    public CarModel getCar(String id) {
        return cars.get(id);
    }

    public Collection<CarModel> getAllCars() {
        return cars.values();
    }

    public void updateCar(CarModel car) {
        cars.put(car.getId(), car);
    }

    public void deleteCar(String id) {
        cars.remove(id);
    }

    // Поиск автомобилей по различным критериям
    public List<CarModel> searchCars(String brand, String model, int year, double minPrice, double maxPrice, String condition) {
        return cars.values().stream()
                .filter(car -> (brand == null || car.getBrand().equalsIgnoreCase(brand)) &&
                        (model == null || car.getModel().equalsIgnoreCase(model)) &&
                        (year <= 0 || car.getYear() == year) &&
                        (minPrice <= 0 || car.getPrice() >= minPrice) &&
                        (maxPrice <= 0 || car.getPrice() <= maxPrice) &&
                        (condition == null || car.getCondition().equalsIgnoreCase(condition)))
                .collect(Collectors.toList());
    }
}
