package hr.javafx.eperformance.connection;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private DatabaseConnection() {}

    public static Connection connectToDatabase() throws IOException, SQLException {
        Properties props = new Properties();
        try (FileReader in = new FileReader("database.properties")) {
            props.load(in);
        }
        return DriverManager.getConnection(
                props.getProperty("databaseUrl"),
                props.getProperty("username"),
                props.getProperty("password")
        );
    }

    public static void disconnectFromDatabase(Connection connection) throws SQLException {
        connection.close();
    }

}
