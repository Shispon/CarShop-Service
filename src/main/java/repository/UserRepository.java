package repository;

import models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserRepository implements CrudRepository<UserModel, Integer> {
    private final List<UserModel> userList= new ArrayList<>();
    private Integer id = 0;

    public UserModel create (UserModel user) {
        if (!isUsernameUnique(user.getUsername())) {
            throw new IllegalArgumentException("Такой пользователь сущетсвует");
        }
        UserModel newUser = user.toBuilder()
                        .id(++id)
                        .build();
        userList.add(newUser);
        return newUser;
    }

    public UserModel read(Integer id) {
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public UserModel update (UserModel user) {
        UserModel existUser = read(user.getId());
        if (existUser != null) {
            UserModel updateUser = existUser.toBuilder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .build();
            userList.set(user.getId(), updateUser);
            return updateUser;
        }
        return null;
    }

    public boolean delete(Integer id) {
       return userList.removeIf(user -> user.getId().equals(id));
    }

    public List<UserModel> findAll() {
        return userList;
    }
    public boolean isUsernameUnique(String username) {
        return userList.stream().noneMatch(user -> user.getUsername().equals(username));
    }

    public boolean authorization(String username, String password) {
        return userList.stream()
                .anyMatch(userModel -> userModel.getUsername().equals(username) &&
                        userModel.getPassword().equals(password));
    }
}
