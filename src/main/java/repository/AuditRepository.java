package repository;

import models.AuditModel;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditRepository implements CrudRepository<AuditModel,Integer> {

    @Override
    public AuditModel create(AuditModel audit) {
        String insertSQL = "INSERT INTO car_shop.audit (action, timestamp, user_id) VALUES (?, ?, ?) RETURNING id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            LocalDateTime timestamp = audit.getTimestamp() != null ? audit.getTimestamp() : LocalDateTime.now();

            statement.setString(1, audit.getAction());
            statement.setTimestamp(2, Timestamp.valueOf(timestamp));
            statement.setInt(3, audit.getUserId());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                return audit.toBuilder().id(id).timestamp(timestamp).build(); // Убедитесь, что возвращаемый объект имеет актуальный timestamp
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось создать log");
        }
        return null;
    }

    @Override
    public AuditModel read(Integer id) {
        String getByIdSQL = "SELECT * FROM car_shop.audit WHERE id=?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(getByIdSQL)) {
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return AuditModel.builder()
                        .id(rs.getInt("id"))
                        .action(rs.getString("action"))
                        .timestamp(rs.getTimestamp("timestamp").toLocalDateTime())
                        .userId(rs.getInt("user_id"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public AuditModel update(AuditModel audit) {
        String updateSQL = "UPDATE car_shop.audit SET action = ?, timestamp = ?, user_id = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {

            statement.setString(1, audit.getAction());
            statement.setTimestamp(2, Timestamp.valueOf(audit.getTimestamp()));
            statement.setInt(3, audit.getUserId());
            statement.setInt(4, audit.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return audit; // Возвращаем обновленный объект
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        String deleteSQL = "DELETE FROM car_shop.audit WHERE id = ?";

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

    @Override
    public List<AuditModel> findAll() {
        List<AuditModel> auditList = new ArrayList<>();
        String getAllSQL = "SELECT * FROM car_shop.audit";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(getAllSQL);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                AuditModel audit = AuditModel.builder()
                        .id(rs.getInt("id"))
                        .action(rs.getString("action"))
                        .timestamp(rs.getTimestamp("timestamp").toLocalDateTime())
                        .userId(rs.getInt("user_id"))
                        .build();
                auditList.add(audit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auditList;
    }
}
