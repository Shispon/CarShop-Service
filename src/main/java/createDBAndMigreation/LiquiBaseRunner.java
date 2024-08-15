package createDBAndMigreation;


import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

public class LiquiBaseRunner {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/db";
    private static final String USER = "admin";
    private static final String PASSWORD = "12345";

    public static void run() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.yaml",
                    new ClassLoaderResourceAccessor(), database);

            liquibase.update(new Contexts());

            System.out.println("Liquibase migrations executed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

