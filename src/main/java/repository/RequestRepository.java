package repository;

import models.RequestModel;
import models.ServiceEnum;
import models.StatusEnum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestRepository implements CrudRepository<RequestModel,Integer> {
    @Override
    public RequestModel create(RequestModel request) {
        String insertSQL = "INSERT INTO car_shop.service_request (car_brand, car_model, username, service_type, status, car_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, request.getCarBrand());
            statement.setString(2, request.getCarModel());
            statement.setString(3, request.getUsername());
            statement.setString(4, request.getServiceType().toString());
            statement.setString(5, request.getStatus().toString());
            statement.setInt(6, request.getCarId());
            statement.setInt(7, request.getUserId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int newId = resultSet.getInt("id");
                    return request.toBuilder().id(newId).build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось создать запрос");
        }
        return null;
    }

    @Override
    public RequestModel read(Integer id) {
        String query = "SELECT id, car_brand, car_model, username, service_type, status, car_id, user_id FROM car_shop.service_request WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return RequestModel.builder()
                            .id(resultSet.getInt("id"))
                            .carBrand(resultSet.getString("car_brand"))
                            .carModel(resultSet.getString("car_model"))
                            .username(resultSet.getString("username"))
                            .serviceType(ServiceEnum.valueOf(resultSet.getString("service_type")))
                            .status(StatusEnum.valueOf(resultSet.getString("status")))
                            .carId(resultSet.getInt("car_id"))
                            .userId(resultSet.getInt("user_id"))
                            .build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Если запрос не найден
    }

    @Override
    public RequestModel update(RequestModel request) {
        String updateSQL = "UPDATE car_shop.service_request SET car_brand = ?, car_model = ?, username = ?, service_type = ?, status = ?, car_id = ?, user_id = ? WHERE id = ? RETURNING id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, request.getCarBrand());
            statement.setString(2, request.getCarModel());
            statement.setString(3, request.getUsername());
            statement.setString(4, request.getServiceType().toString());
            statement.setString(5, request.getStatus().toString());
            statement.setInt(6, request.getCarId());
            statement.setInt(7, request.getUserId());
            statement.setInt(8, request.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return request.toBuilder().id(resultSet.getInt("id")).build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось обновить запрос");
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        String deleteSQL = "DELETE FROM car_shop.service_request WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Если хотя бы одна строка была удалена
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<RequestModel> findAll() {
        List<RequestModel> requests = new ArrayList<>();
        String query = "SELECT id, car_brand, car_model, username, service_type, status, car_id, user_id FROM car_shop.service_request";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                RequestModel request = RequestModel.builder()
                        .id(resultSet.getInt("id"))
                        .carBrand(resultSet.getString("car_brand"))
                        .carModel(resultSet.getString("car_model"))
                        .username(resultSet.getString("username"))
                        .serviceType(ServiceEnum.valueOf(resultSet.getString("service_type")))
                        .status(StatusEnum.valueOf(resultSet.getString("status")))
                        .carId(resultSet.getInt("car_id"))
                        .userId(resultSet.getInt("user_id"))
                        .build();
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
}
