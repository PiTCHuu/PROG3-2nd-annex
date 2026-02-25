package modules;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() {
        try {
            String jdbcURl = System.getenv("jdbc:postgresql://localhost:5432/annex_db");
            String user = System.getenv("postgres");
            String password = System.getenv("123");
            return DriverManager.getConnection(jdbcURl, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}