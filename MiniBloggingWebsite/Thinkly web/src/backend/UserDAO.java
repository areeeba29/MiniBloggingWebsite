package backend;

import java.sql.*;

public class UserDAO {
    private Connection conn;

    public UserDAO() { conn = DatabaseConnection.getInstance().getConnection(); }

    public boolean insertUser(User user) {
        if (user.getUsername().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            System.out.println("Fields cannot be empty!"); return false;
        }
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.out.println("Invalid email format!"); return false;
        }
        if (user.getPassword().length() < 5) {
            System.out.println("Password must be at least 5 characters."); return false;
        }

        String sql = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("User Insert Error: " + e.getMessage());
            return false;
        }
    }

    public boolean checkLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) return false;
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
            return false;
        }
    }

    public int getUserId(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) { System.out.println("GetUserId Error: " + e.getMessage()); }
        return -1;
    }

    public boolean checkEmailExists(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeQuery().next();
        } catch (SQLException e) { System.out.println("Email Check Error: " + e.getMessage()); }
        return false;
    }
}
