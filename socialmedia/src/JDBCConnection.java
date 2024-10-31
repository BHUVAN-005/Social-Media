import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            String url = "jdbc:mysql://localhost:3306/connect";  
            String user = "root";  
            String password = "root";  

            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); 
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to the database!");
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found Exception: " + e.getMessage());
            }
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.out.println("Error closing the connection: " + e.getMessage());
        }
    }
}
