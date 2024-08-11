package service;

import models.UserModel;
import repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;
    private UserModel currentUser;  // Добавляем поле для отслеживания текущего пользователя

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentUser = null; // Изначально текущий пользователь не определен
    }

    public void registerUser(UserModel user) {
        userRepository.create(user);
    }

    public boolean authenticateUser(String username, String password) {
        boolean isAuthenticated = userRepository.authorization(username, password);
        if (isAuthenticated) {
            this.currentUser = userRepository.findByUsername(username); // Загружаем данные пользователя после успешной аутентификации
        } else {
            this.currentUser = null; // Если аутентификация не удалась, текущий пользователь также должен быть null
        }
        return isAuthenticated;
    }

    public void logout() {
        this.currentUser = null;
    }

    public UserModel getCurrentUser() {
        return this.currentUser; // Возвращаем текущего пользователя
    }

    public Integer getCurrentUserId() {
        return this.currentUser != null ? this.currentUser.getId() : null;
    }
}
