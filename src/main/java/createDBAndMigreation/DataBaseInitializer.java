package createDBAndMigreation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataBaseInitializer {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/db";
    private static final String USER = "admin";
    private static final String PASSWORD = "12345";

    public static void run() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            String createSchemaSQL = "CREATE SCHEMA IF NOT EXISTS car_shop";
            statement.execute(createSchemaSQL);

            System.out.println("Schema 'carshop' created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
