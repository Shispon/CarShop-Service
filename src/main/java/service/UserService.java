package service;

import models.UserModel;
import repository.UserRepository;


public class UserService {
    private final UserRepository userRepository;
    private Integer id;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        id = 0;
    }

    public void registerUser(UserModel user) {
        UserModel newUser = user.toBuilder()
                .id(id++)
                .build();
        userRepository.create(newUser);
    }

    public boolean authenticateUser(String username, String password) {
       return userRepository.authorization(username, password);
    }
}
