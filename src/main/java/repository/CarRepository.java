package repository;

import models.CarModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {

    public CarModel create(CarModel car) {
        String insertSQL = "INSERT INTO car_shop.car (brand, model, year, price, condition) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try(Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setDouble(4, car.getPrice());
            statement.setString(5, car.getCondition());
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Integer newId = rs.getInt("id");
                return car.toBuilder()
                        .id(newId)
                        .build();

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Не удалось создать пользователя");
    }

    public CarModel read(Integer id) { //поиск по Id
       String getByIdSQL = "SELECT * FROM car_shop.car WHERE id = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(getByIdSQL);){
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                return CarModel.builder()
                        .id(rs.getInt("id"))
                        .brand(rs.getString("brand"))
                        .model(rs.getString("model"))
                        .year(rs.getInt("year"))
                        .price(rs.getDouble("price"))
                        .condition(rs.getString("condition"))
                        .build();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CarModel update(CarModel car) {
        String updateSQL = "UPDATE car_shop.car SET brand = ?, model = ?, year = ?, price = ?, condition = ? WHERE id = ? RETURNING id";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setDouble(4, car.getPrice());
            statement.setString(5, car.getCondition());
            statement.setInt(6, car.getId());

            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                return car.toBuilder().id(rs.getInt("id")).build();
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(Integer id) {
        String deleteSQL = "DELETE FROM car_shop.car WHERE id = ?";

        try(Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<CarModel> findAll() {
        List<CarModel> carList = new ArrayList<>();
        String findAllSQL = "SELECT * FROM car_shop.car";

        try(Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery(findAllSQL);
            while(rs.next()) {
                CarModel car = CarModel.builder()
                        .id(rs.getInt("id"))
                        .brand(rs.getString("brand"))
                        .model(rs.getString("model"))
                        .year(rs.getInt("year"))
                        .price(rs.getDouble("price"))
                        .condition(rs.getString("condition"))
                        .build();
                carList.add(car);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return carList;
    }

    public List<CarModel> findCarsByModel(String carModel) {
        List<CarModel> resultList = new ArrayList<>();
        String findCarsByModelSQL = "SELECT * FROM car_shop.car WHERE model = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(findCarsByModelSQL)) {
            statement.setString(1, carModel);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                CarModel car = CarModel.builder()
                        .id(rs.getInt("id"))
                        .brand(rs.getString("brand"))
                        .model(rs.getString("model"))
                        .year(rs.getInt("year"))
                        .price(rs.getDouble("price"))
                        .condition(rs.getString("condition"))
                        .build();
                resultList.add(car);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public List<CarModel> findCarsByBrand(String brand) {
        List<CarModel> resultList = new ArrayList<>();
        String findCarsByBrand = "SELECT * FROM car_shop.car WHERE brand = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(findCarsByBrand)) {
            statement.setString(1, brand);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                CarModel car = CarModel.builder()
                        .id(rs.getInt("id"))
                        .brand(rs.getString("brand"))
                        .model(rs.getString("model"))
                        .year(rs.getInt("year"))
                        .price(rs.getDouble("price"))
                        .condition(rs.getString("condition"))
                        .build();
                resultList.add(car);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public List<CarModel> findCarsByYear(Integer year) {
        List<CarModel> resultList = new ArrayList<>();
        String findCarsByBrand = "SELECT * FROM car_shop.car WHERE year = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(findCarsByBrand)) {
            statement.setInt(1, year);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                CarModel car = CarModel.builder()
                        .id(rs.getInt("id"))
                        .brand(rs.getString("brand"))
                        .model(rs.getString("model"))
                        .year(rs.getInt("year"))
                        .price(rs.getDouble("price"))
                        .condition(rs.getString("condition"))
                        .build();
                resultList.add(car);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public List<CarModel> findCarByPrice(double minPrice, double maxPrice) {
        List<CarModel> resultList = new ArrayList<>();
        String findCarsByPrice = "SELECT * FROM car_shop.car WHERE price >= ? AND price <= ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(findCarsByPrice)) {
            statement.setDouble(1, minPrice);
            statement.setDouble(2, maxPrice);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                CarModel car = CarModel.builder()
                        .id(rs.getInt("id"))
                        .brand(rs.getString("brand"))
                        .model(rs.getString("model"))
                        .year(rs.getInt("year"))
                        .price(rs.getDouble("price"))
                        .condition(rs.getString("condition"))
                        .build();
                resultList.add(car);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public List<CarModel> findCarsByCondition(String condition) {
        List<CarModel> resultList = new ArrayList<>();
        String findCarsByBrand = "SELECT * from car_shop.car WHERE condition = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(findCarsByBrand)) {
            statement.setString(1, condition);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                CarModel car = CarModel.builder()
                        .id(rs.getInt("id"))
                        .brand(rs.getString("brand"))
                        .model(rs.getString("model"))
                        .year(rs.getInt("year"))
                        .price(rs.getDouble("price"))
                        .condition(rs.getString("condition"))
                        .build();
                resultList.add(car);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
