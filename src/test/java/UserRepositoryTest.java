

import models.RoleEnum;
import models.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import repository.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("test_db")
            .withUsername("admin")
            .withPassword("12345");

    private Connection connection;
    private UserRepository userRepository;

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
            statement.execute("CREATE TABLE IF NOT EXISTS car_shop.user (" +
                    "id SERIAL PRIMARY KEY, " +
                    "username VARCHAR(255) UNIQUE, " +
                    "password VARCHAR(255), " +
                    "role VARCHAR(50))");
        }

        // Установите соединение для репозитория
        userRepository = new UserRepository();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Очистка таблицы после тестов
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS car_shop.user");
        }
        connection.close();

        // Останавливаем контейнер PostgreSQL
        postgresContainer.stop();
    }

    @Test
    public void testCreate() {
        UserModel user = UserModel.builder()
                .username("testuser")
                .password("password")
                .role(RoleEnum.CLIENT)
                .build();

        UserModel createdUser = userRepository.create(user);
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(user.getUsername(), createdUser.getUsername());
    }

    @Test
    public void testRead() {
        UserModel user = UserModel.builder()
                .username("testuser")
                .password("password")
                .role(RoleEnum.MANAGER)
                .build();
        UserModel createdUser = userRepository.create(user);

        UserModel readUser = userRepository.read(createdUser.getId());
        assertNotNull(readUser);
        assertEquals(createdUser.getId(), readUser.getId());
        assertEquals(createdUser.getUsername(), readUser.getUsername());
    }

    @Test
    public void testUpdate() {
        UserModel user = UserModel.builder()
                .username("testuser")
                .password("password")
                .role(RoleEnum.ADMIN)
                .build();
        UserModel createdUser = userRepository.create(user);

        UserModel updatedUser = createdUser.toBuilder()
                .username("updateduser")
                .build();
        UserModel result = userRepository.update(updatedUser);
        assertNotNull(result);
        assertEquals("updateduser", result.getUsername());
    }

    @Test
    public void testDelete() {
        UserModel user = UserModel.builder()
                .username("testuser")
                .password("password")
                .role(RoleEnum.ADMIN)
                .build();
        UserModel createdUser = userRepository.create(user);

        boolean isDeleted = userRepository.delete(createdUser.getId());
        assertTrue(isDeleted);
        assertNull(userRepository.read(createdUser.getId()));
    }

    @Test
    public void testFindAll() {
        UserModel user1 = UserModel.builder()
                .username("user1")
                .password("password1")
                .role(RoleEnum.MANAGER)
                .build();
        UserModel user2 = UserModel.builder()
                .username("user2")
                .password("password2")
                .role(RoleEnum.ADMIN)
                .build();

        userRepository.create(user1);
        userRepository.create(user2);

        List<UserModel> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertTrue(users.stream().anyMatch(u -> "user1".equals(u.getUsername())));
        assertTrue(users.stream().anyMatch(u -> "user2".equals(u.getUsername())));
    }

    @Test
    public void testIsUsernameUnique() {
        UserModel user = UserModel.builder()
                .username("uniqueuser")
                .password("password")
                .role(RoleEnum.ADMIN)
                .build();
        userRepository.create(user);

        boolean isUnique = userRepository.isUsernameUnique("uniqueuser");
        assertFalse(isUnique);

        boolean isUniqueNew = userRepository.isUsernameUnique("newuser");
        assertTrue(isUniqueNew);
    }
}
