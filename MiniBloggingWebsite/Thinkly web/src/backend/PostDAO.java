package backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    private Connection conn;

    public PostDAO() { conn = DatabaseConnection.getInstance().getConnection(); }

    public boolean insertPost(Post post, int authorId) {
        String sql = "INSERT INTO posts(title, content, author_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getContent());
            stmt.setInt(3, authorId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Insert Post Error: " + e.getMessage());
            return false;
        }
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT posts.id, posts.title, posts.content, users.username FROM posts " +
                     "INNER JOIN users ON posts.author_id = users.id ORDER BY posts.id DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User author = new NormalUser(rs.getString("username"), "", "");
                posts.add(new Post(rs.getString("title"), rs.getString("content"), author));
            }
        } catch (SQLException e) {
            System.out.println("Load Posts Error: " + e.getMessage());
        }
        return posts;
    }
}
