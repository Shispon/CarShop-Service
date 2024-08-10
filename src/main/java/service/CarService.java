package service;


import models.CarModel;
import repository.CarRepository;

import java.util.List;

public class CarService {
    CarRepository carRepository;
    private Integer id;
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
        id = 0;
    }
    public void addCar (CarModel car) {
        CarModel newCar = car.toBuilder()
                .id(id++)
                .build();
        carRepository.create(newCar);
    }

    public CarModel getCarById(Integer id) {
        return carRepository.read(id);
    }

    public boolean updateCar(CarModel car) {
        return carRepository.update(car) != null;
    }

    public boolean deleteCar(Integer id) {
        return carRepository.delete(id);
    }
    public List<CarModel> getAllCars() {
        return carRepository.findAll();
    }

    public List<CarModel> searchCarsByBrand(String brand) {
        return carRepository.findCarsByBrand(brand);
    }

    public List<CarModel> searchCarsByModel(String model) {
        return carRepository.findCarsByModel(model);
    }

    public List<CarModel> searchCarsByYear(Integer year) {
        return carRepository.findCarsByYear(year);
    }
    public List<CarModel> searchCarsByPrice(double minPrice, double maxPrice) {
        return carRepository.findCarByPrice(minPrice,maxPrice);
    }
    public List<CarModel> searchCarsByCondition(String condition) {
        return carRepository.findCarsByCondition(condition);
    }
}
