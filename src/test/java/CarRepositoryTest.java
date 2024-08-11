

import models.CarModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import repository.CarRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarRepositoryTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("test_db")
            .withUsername("admin")
            .withPassword("12345");

    private Connection connection;
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        // Запускаем контейнер PostgreSQL
        postgresContainer.start();

        // Подключение к базе данных в контейнере
        connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());

        // Создание таблицы для тестирования
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS car_shop.car (" +
                    "id SERIAL PRIMARY KEY, " +
                    "brand VARCHAR(255), " +
                    "model VARCHAR(255), " +
                    "year INT, " +
                    "price DOUBLE PRECISION, " +
                    "condition VARCHAR(50))");
        }

        // Установите соединение для репозитория
        carRepository = new CarRepository();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Очистка таблицы после тестов
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS car_shop.car");
        }
        connection.close();

        // Останавливаем контейнер PostgreSQL
        postgresContainer.stop();
    }

    @Test
    public void testCreate() {
        CarModel car = CarModel.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .price(20000.00)
                .condition("New")
                .build();

        CarModel createdCar = carRepository.create(car);
        assertNotNull(createdCar);
        assertNotNull(createdCar.getId());
        assertEquals(car.getBrand(), createdCar.getBrand());
    }

    @Test
    public void testRead() {
        CarModel car = CarModel.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .price(20000.00)
                .condition("New")
                .build();
        CarModel createdCar = carRepository.create(car);

        CarModel readCar = carRepository.read(createdCar.getId());
        assertNotNull(readCar);
        assertEquals(createdCar.getId(), readCar.getId());
        assertEquals(createdCar.getModel(), readCar.getModel());
    }

    @Test
    public void testUpdate() {
        CarModel car = CarModel.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .price(20000.00)
                .condition("New")
                .build();
        CarModel createdCar = carRepository.create(car);

        CarModel updatedCar = createdCar.toBuilder()
                .price(18000.00)
                .build();
        CarModel result = carRepository.update(updatedCar);
        assertNotNull(result);
        assertEquals(18000.00, result.getPrice());
    }

    @Test
    public void testDelete() {
        CarModel car = CarModel.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .price(20000.00)
                .condition("New")
                .build();
        CarModel createdCar = carRepository.create(car);

        boolean isDeleted = carRepository.delete(createdCar.getId());
        assertTrue(isDeleted);
        assertNull(carRepository.read(createdCar.getId()));
    }

    @Test
    public void testFindAll() {
        CarModel car1 = CarModel.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .price(20000.00)
                .condition("New")
                .build();
        CarModel car2 = CarModel.builder()
                .brand("Honda")
                .model("Civic")
                .year(2021)
                .price(22000.00)
                .condition("Used")
                .build();

        carRepository.create(car1);
        carRepository.create(car2);

        List<CarModel> cars = carRepository.findAll();
        assertFalse(cars.isEmpty());
        assertTrue(cars.stream().anyMatch(c -> "Toyota".equals(c.getBrand())));
        assertTrue(cars.stream().anyMatch(c -> "Honda".equals(c.getBrand())));
    }

    @Test
    public void testFindCarsByModel() {
        CarModel car1 = CarModel.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .price(20000.00)
                .condition("New")
                .build();
        CarModel car2 = CarModel.builder()
                .brand("Honda")
                .model("Civic")
                .year(2021)
                .price(22000.00)
                .condition("Used")
                .build();

        carRepository.create(car1);
        carRepository.create(car2);

        List<CarModel> cars = carRepository.findCarsByModel("Corolla");
        assertFalse(cars.isEmpty());
        assertEquals(1, cars.size());
        assertEquals("Corolla", cars.get(0).getModel());
    }

    @Test
    public void testFindCarsByBrand() {
        CarModel car1 = CarModel.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .price(20000.00)
                .condition("New")
                .build();
        CarModel car2 = CarModel.builder()
                .brand("Honda")
                .model("Civic")
                .year(2021)
                .price(22000.00)
                .condition("Used")
                .build();

        carRepository.create(car1);
        carRepository.create(car2);

        List<CarModel> cars = carRepository.findCarsByBrand("Honda");
        assertFalse(cars.isEmpty());
        assertEquals(1, cars.size());
        assertEquals("Honda", cars.get(0).getBrand());
    }

    @Test
    public void testFindCarsByYear() {
        CarModel car1 = CarModel.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .price(20000.00)
                .condition("New")
                .build();
        CarModel car2 = CarModel.builder()
                .brand("Honda")
                .model("Civic")
                .year(2021)
                .price(22000.00)
                .condition("Used")
                .build();

        carRepository.create(car1);
        carRepository.create(car2);

        List<CarModel> cars = carRepository.findCarsByYear(2021);
        assertFalse(cars.isEmpty());
        assertEquals(1, cars.size());
        assertEquals(2021, cars.get(0).getYear());
    }

    @Test
    public void testFindCarsByPrice() {
        CarModel car1 = CarModel.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .price(20000.00)
                .condition("New")
                .build();
        CarModel car2 = CarModel.builder()
                .brand("Honda")
                .model("Civic")
                .year(2021)
                .price(22000.00)
                .condition("Used")
                .build();

        carRepository.create(car1);
        carRepository.create(car2);

        List<CarModel> cars = carRepository.findCarByPrice(19000.00, 21000.00);
        assertFalse(cars.isEmpty());
        assertEquals(1, cars.size());
        assertEquals(20000.00, cars.get(0).getPrice());
    }

    @Test
    public void testFindCarsByCondition() {
        CarModel car1 = CarModel.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .price(20000.00)
                .condition("New")
                .build();
        CarModel car2 = CarModel.builder()
                .brand("Honda")
                .model("Civic")
                .year(2021)
                .price(22000.00)
                .condition("Used")
                .build();

        carRepository.create(car1);
        carRepository.create(car2);

        List<CarModel> cars = carRepository.findCarsByCondition("Used");
        assertFalse(cars.isEmpty());
        assertEquals(1, cars.size());
        assertEquals("Used", cars.get(0).getCondition());
    }
}
