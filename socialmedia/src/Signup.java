import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Signup {

    public static void registerUser(String username, String email, String password, String profilePic) {
        Connection connection = JDBCConnection.getConnection();   
        String insertQuery = "INSERT INTO users(username, email, password, profile_picture) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(insertQuery);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, profilePic);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User registered successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Username: ");
        String username = sc.nextLine();

        System.out.println("Enter Email: ");
        String email = sc.nextLine();

        System.out.println("Enter Password: ");
        String password = sc.nextLine();

        System.out.println("Enter Profile Picture URL: ");
        String profilePic = sc.nextLine();

       
        registerUser(username, email, password, profilePic);
    }
}
