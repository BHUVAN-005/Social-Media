import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {

    public static boolean loginUser(String username, String password) {
        Connection connection = JDBCConnection.getConnection();
        String selectQuery = "SELECT * FROM users WHERE username = ? AND password = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(selectQuery);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                System.out.println("Login successful. Welcome, " + resultSet.getString("username"));
                return true;
            } else {
                System.out.println("Invalid username or password.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getUserIdByUsername(String username) {
        Connection connection = JDBCConnection.getConnection();
        String userQuery = "SELECT user_id FROM users WHERE username = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(userQuery);
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if user not found
    }

    public static void viewPosts() {
        Connection connection = JDBCConnection.getConnection();
        String selectQuery = "SELECT content, image_url FROM posts";

        try {
            PreparedStatement ps = connection.prepareStatement(selectQuery);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                System.out.println("Post: " + resultSet.getString("content"));
                String imageUrl = resultSet.getString("image_url");
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    System.out.println("Image: " + imageUrl);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void messageFollowers(String followerName, int senderId) {
        Connection connection = JDBCConnection.getConnection();
        Scanner sc = new Scanner(System.in);

        try {
            // Find the follower by name
            String followerQuery = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(followerQuery);
            ps.setString(1, followerName);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                int followerId = resultSet.getInt("user_id");

                // Ask for the message to send
                System.out.println("Enter the message: ");
                String message = sc.nextLine();

                // Insert the message into the messages table
                String messageQuery = "INSERT INTO messages (sender_id, receiver_id, message_text) VALUES (?, ?, ?)";
                ps = connection.prepareStatement(messageQuery);
                ps.setInt(1, senderId); // Use the actual sender's user ID
                ps.setInt(2, followerId);
                ps.setString(3, message);
                ps.executeUpdate();

                System.out.println("Message sent successfully.");
            } else {
                System.out.println("Follower not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewMessagesSentToMe(int userId) {
        Connection connection = JDBCConnection.getConnection();
        String messageQuery = "SELECT m.message_text, u.username FROM messages m JOIN users u ON m.sender_id = u.user_id WHERE m.receiver_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(messageQuery);
            ps.setInt(1, userId);
            ResultSet resultSet = ps.executeQuery();

            System.out.println("Messages sent to you:");
            boolean hasMessages = false;
            while (resultSet.next()) {
                hasMessages = true;
                System.out.println("From " + resultSet.getString("username") + ": " + resultSet.getString("message_text"));
            }
            if (!hasMessages) {
                System.out.println("You have no messages.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Username: ");
        String username = sc.nextLine();

        System.out.println("Enter Password: ");
        String password = sc.nextLine();

        // Check user credentials
        if (loginUser(username, password)) {
            int loggedInUserId = getUserIdByUsername(username);
            if (loggedInUserId == -1) {
                System.out.println("Error retrieving logged-in user ID.");
                return;
            }

            while (true) {
                System.out.printf("Enter \n1. View Posts\n2. Message Followers by Name\n3. View Messages Sent to Me\n4. Exit\n");
                int a = sc.nextInt();
                sc.nextLine();  // Consume newline

                switch (a) {
                    case 1:
                        viewPosts();
                        break;
                    case 2:
                        System.out.println("Enter the follower's name: ");
                        String followerName = sc.nextLine();
                        messageFollowers(followerName, loggedInUserId);
                        break;
                    case 3:
                        viewMessagesSentToMe(loggedInUserId);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                        break;
                }
            }
        }
    }
}
