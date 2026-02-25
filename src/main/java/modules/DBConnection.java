package modules;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public Connection getConnection() {
        try {
            String jdbcURL = "jdbc:postgresql://localhost:5432/annex2_db";
            String user = "postgres";
            String password = "123";

            return DriverManager.getConnection(jdbcURL, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}