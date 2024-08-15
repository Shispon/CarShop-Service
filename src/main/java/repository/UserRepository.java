package repository;

import models.RoleEnum;
import models.UserModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements CrudRepository<UserModel, Integer>  {

    public boolean isUsernameUnique(String username) {
        String query = "SELECT COUNT(*) FROM car_shop.user WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Создание нового пользователя
    public UserModel create(UserModel user) {
        if (!isUsernameUnique(user.getUsername())) {
            throw new IllegalArgumentException("Такой пользователь существует");
        }
        String insertSQL = "INSERT INTO car_shop.user (username, password, role) VALUES (?, ?, ?) RETURNING id";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int newId = resultSet.getInt("id");
                return user.toBuilder()
                        .id(newId)
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось создать пользователя");
        }
        return null;
    }

    public UserModel read(Integer id) {
        String query = "SELECT id, username, password, role FROM car_shop.user WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

             ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return UserModel.builder()
                            .id(resultSet.getInt("id"))
                            .username(resultSet.getString("username"))
                            .password(resultSet.getString("password"))
                            .role(RoleEnum.valueOf(resultSet.getString("role")))
                            .build();
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserModel update(UserModel user) {
        String updateSQL = "UPDATE car_shop.user SET username = ?, password = ?, role = ? WHERE id = ? RETURNING id";
        List<UserModel> userList = findByUsernameForUpdate(user.getUsername());
        for (UserModel u : userList) {
            if (!user.getId().equals(u.getId())) {
                throw new RuntimeException("Не удалось обновить пользователя");
            }
        }
        if (!isUsernameUnique(user.getUsername())) {
            throw new IllegalArgumentException("Такой пользователь существует");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());
            statement.setInt(4, user.getId());

            ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return user.toBuilder().id(resultSet.getInt("id")).build();
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean delete(Integer id) {
        String deleteSQL = "DELETE FROM car_shop.user WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<UserModel> findAll() {
        List<UserModel> users = new ArrayList<>();
        String query = "SELECT id, username, password, role FROM car_shop.user";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                UserModel user = UserModel.builder()
                        .id(resultSet.getInt("id"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .role(RoleEnum.valueOf(resultSet.getString("role")))
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public boolean authorization(String username, String password) {
        String authorizationSQL = "SELECT id FROM car_shop.user WHERE username = ? AND password = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(authorizationSQL)) {
            statement.setString(1,username);
            statement.setString(2,password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private List<UserModel> findByUsernameForUpdate(String username) {
        List<UserModel> users = new ArrayList<>();
        String findByUsernameSQL = "SELECT * FROM car_shop.user WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(findByUsernameSQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UserModel user = UserModel.builder()
                        .id(resultSet.getInt("id"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .role(RoleEnum.valueOf(resultSet.getString("role")))
                        .build();
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public UserModel findByUsername(String username) {
        String findByUsernameSQL = "SELECT * FROM car_shop.user WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(findByUsernameSQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return UserModel.builder()
                        .id(resultSet.getInt("id"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .role(RoleEnum.valueOf(resultSet.getString("role")))
                        .build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
