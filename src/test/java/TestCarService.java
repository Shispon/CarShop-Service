import Models.CarModel;
import Service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class TestCarService {
    private CarService carService;

    @BeforeEach
    void setUp() {
        carService = new CarService();
    }

    @Test
    void addCar_ShouldAddCarToMap() {
        CarModel car = new CarModel("Toyota", "Camry", 2020, 30000, "new");
        carService.addCar(car);
        assertFalse(carService.getAllCars().isEmpty());
    }

    @Test
    void getCar_ShouldReturnCorrectCar() {
        CarModel car = new CarModel("Toyota", "Camry", 2020, 30000, "new");
        carService.addCar(car);
        CarModel result = carService.getCar(car.getId());
        assertEquals(car.getId(), result.getId());
    }

    @Test
    void updateCar_ShouldUpdateCarInMap() {
        CarModel car = new CarModel("Toyota", "Camry", 2020, 30000, "new");
        carService.addCar(car);
        car.setPrice(25000);
        carService.updateCar(car);
        CarModel result = carService.getCar(car.getId());
        assertEquals(25000, result.getPrice());
    }

    @Test
    void deleteCar_ShouldRemoveCarFromMap() {
        CarModel car = new CarModel("Toyota", "Camry", 2020, 30000, "new");
        carService.addCar(car);
        carService.deleteCar(car.getId());
        assertNull(carService.getCar(car.getId()));
    }

    @Test
    void searchCars_ShouldReturnCorrectCars() {
        CarModel car1 = new CarModel("Toyota", "Camry", 2020, 30000, "new");
        CarModel car2 = new CarModel("Honda", "Accord", 2019, 25000, "used");
        carService.addCar(car1);
        carService.addCar(car2);

        List<CarModel> result = carService.searchCars("Toyota", "Camry", 2020, 0, 0, null);
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getBrand());
        assertEquals("Camry", result.get(0).getModel());
        assertEquals(2020, result.get(0).getYear());
    }

    @Test
    void searchCars_ShouldReturnCarsWithinPriceRange() {
        CarModel car1 = new CarModel("Toyota", "Camry", 2020, 30000, "new");
        CarModel car2 = new CarModel("Honda", "Accord", 2019, 25000, "used");
        carService.addCar(car1);
        carService.addCar(car2);

        List<CarModel> result = carService.searchCars(null, null, 0, 20000, 30000, null);
        assertEquals(2, result.size());
    }

    @Test
    void searchCars_ShouldReturnCarsMatchingCondition() {
        CarModel car1 = new CarModel("Toyota", "Camry", 2020, 30000, "new");
        CarModel car2 = new CarModel("Honda", "Accord", 2019, 25000, "used");
        carService.addCar(car1);
        carService.addCar(car2);

        List<CarModel> result = carService.searchCars(null, null, 0, 0, 0, "used");
        assertEquals(1, result.size());
        assertEquals("used", result.get(0).getCondition());
    }
}
