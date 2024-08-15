package repository;

import models.OrderModel;
import models.StatusEnum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements CrudRepository<OrderModel,Integer>{
    public OrderModel create(OrderModel order) {
        String insertSQL = "INSERT INTO car_shop.order (car_id, user_id, status) VALUES (?, ?, ?) RETURNING id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setInt(1, order.getCarId());
            statement.setInt(2, order.getUserId());
            statement.setString(3, order.getStatus().toString());

            ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int newId = resultSet.getInt("id");
                    return order.toBuilder().id(newId).build();
                }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось создать заказ");
        }
        return null;
    }

    // Метод чтения заказа по ID
    public OrderModel read(Integer id) {
        String query = "SELECT * FROM car_shop.order WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return OrderModel.builder()
                            .id(resultSet.getInt("id"))
                            .carId(resultSet.getInt("car_id"))
                            .userId(resultSet.getInt("user_id"))
                            .status(StatusEnum.valueOf(resultSet.getString("status")))
                            .build();
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Метод обновления существующего заказа
    public OrderModel update(OrderModel order) {
        String updateSQL = "UPDATE car_shop.order SET car_id = ?, user_id = ?, status = ? WHERE id = ? RETURNING id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setInt(1, order.getCarId());
            statement.setInt(2, order.getUserId());
            statement.setString(3, order.getStatus().toString());
            statement.setInt(4, order.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return order.toBuilder().id(resultSet.getInt("id")).build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Метод удаления заказа по ID
    public boolean delete(Integer id) {
        String deleteSQL = "DELETE FROM car_shop.order WHERE id = ?";

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

    // Метод для получения всех заказов
    public List<OrderModel> findAll() {
        List<OrderModel> orders = new ArrayList<>();
        String query = "SELECT id, car_id, user_id, status FROM car_shop.order";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                OrderModel order = OrderModel.builder()
                        .id(resultSet.getInt("id"))
                        .carId(resultSet.getInt("car_id"))
                        .userId(resultSet.getInt("user_id"))
                        .status(StatusEnum.valueOf(resultSet.getString("status")))
                        .build();
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Метод для поиска заказов по userId и carId
    public List<OrderModel> findOrderByUserIdAndCarId(Integer userId, Integer carId) {
        List<OrderModel> orders = new ArrayList<>();
        String query = "SELECT id, car_id, user_id, status FROM car_shop.order WHERE user_id = ? AND car_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, carId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OrderModel order = OrderModel.builder()
                            .id(resultSet.getInt("id"))
                            .carId(resultSet.getInt("car_id"))
                            .userId(resultSet.getInt("user_id"))
                            .status(StatusEnum.valueOf(resultSet.getString("status")))
                            .build();
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
