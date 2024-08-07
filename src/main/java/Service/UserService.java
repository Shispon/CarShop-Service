package Service;


import Models.UserModel;

import java.util.*;
import java.util.stream.Collectors;

public class UserService {

    private Map<String, UserModel> users = new HashMap<>();
    private UserModel loggedInUser = null;

    public void registerUser(UserModel user) {
        if (users.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }
        users.put(user.getUsername(), user);
    }
    public void addUser(UserModel user) {
        users.put(user.getId(), user);
    }

    public UserModel authenticateUser(String username, String password) {
        UserModel user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUser = user;
            return user;
        }
        return null;
    }
    public Map<String, UserModel> getUsers() {
        return users;
    }
    public List<UserModel> filterByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }
    public UserModel getUser(String id) {
        return users.get(id);
    }


    public List<UserModel> sortByUsername() {
        return users.values().stream()
                .sorted(Comparator.comparing(UserModel::getUsername))
                .collect(Collectors.toList());
    }


    public List<UserModel> sortByRole() {
        return users.values().stream()
                .sorted(Comparator.comparing(UserModel::getRole))
                .collect(Collectors.toList());
    }
    public void logoutUser() {
        loggedInUser = null;
    }
    public Collection<UserModel> getAllUsers() {
        return users.values();
    }

    public void updateUser(UserModel user) {
        if (users.containsKey(user.getUsername())) {
            users.put(user.getUsername(), user);
        }
    }

    public void deleteUser(String username) {
        users.remove(username);
    }
}
