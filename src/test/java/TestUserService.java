import Models.UserModel;
import Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class TestUserService {
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void addUser_ShouldAddUserToMap() {
        UserModel user = new UserModel("Alice", "password123", "admin");
        userService.addUser(user);
        assertEquals(1, userService.getUsers().size());
    }

    @Test
    void filterByUsername_ShouldReturnCorrectUsers() {
        UserModel user1 = new UserModel("Alice", "password123", "admin");
        UserModel user2 = new UserModel("Bob", "password456", "user");
        userService.addUser(user1);
        userService.addUser(user2);

        List<UserModel> result = userService.filterByUsername("Alice");
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getUsername());
    }

    @Test
    void filterByRole_ShouldReturnCorrectUsers() {
        UserModel user1 = new UserModel("Alice", "password123", "admin");
        UserModel user2 = new UserModel("Bob", "password456", "user");
        userService.addUser(user1);
        userService.addUser(user2);

        List<UserModel> result = userService.filterByUsername("Alice");
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getRole());
    }
}
