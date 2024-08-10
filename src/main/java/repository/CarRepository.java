package repository;

import models.CarModel;

import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private final List<CarModel> carList= new ArrayList<>();
    private Integer id = 0;

    public CarModel create(CarModel car) {
        CarModel newCar = car.toBuilder()
                .id(++id)
                .build();
        carList.add(newCar);
        return newCar;
    }

    public CarModel read(Integer id) { //поиск по Id
        return carList.stream()
                .filter(car -> car.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public CarModel update(CarModel car) {
        CarModel existingCar = read(car.getId());
        if (existingCar != null) {
            CarModel updatedCar = existingCar.toBuilder()
                    .brand(car.getBrand())
                    .model(car.getModel())
                    .year(car.getYear())
                    .price(car.getPrice())
                    .condition(car.getCondition())
                    .build();
            carList.set(carList.indexOf(existingCar), updatedCar);
            return updatedCar;
        }
        return null;
    }

    public boolean delete(Integer id) {
        return carList.removeIf(car -> car.getId().equals(id));
    }

    public List<CarModel> findAll() {
        return new ArrayList<>(carList); // Возвращаем копию списка для сохранения инкапсуляции
    }

    public List<CarModel> findCarsByModel(String carModel) {
        List<CarModel> resultList = new ArrayList<>();
        carList.stream()
                .filter(car -> car.getModel().equals(carModel))
                .forEach(resultList::add);
        return resultList;
    }

    public List<CarModel> findCarsByBrand(String brand) {
        List<CarModel> resultList = new ArrayList<>();
        carList.stream()
                .filter(car -> car.getBrand().equals(brand))
                .forEach(resultList::add);
        return resultList;
    }

    public List<CarModel> findCarsByYear(Integer year) {
        List<CarModel> resultList = new ArrayList<>();
        carList.stream()
                .filter(car -> car.getYear().equals(year))
                .forEach(resultList::add);
        return resultList;
    }

    public List<CarModel> findCarByPrice(double minPrice, double maxPrice) {
        List<CarModel> resultList = new ArrayList<>();
        carList.stream().filter(car -> car.getPrice()>=minPrice&&car.getPrice()<=maxPrice)
                .forEach(resultList::add);
        return resultList;
    }

    public List<CarModel> findCarsByCondition(String condition) {
        List<CarModel> resultList = new ArrayList<>();
        carList.stream()
                .filter(car -> car.getCondition().equals(condition))
                .forEach(resultList::add);
        return resultList;
    }
}
