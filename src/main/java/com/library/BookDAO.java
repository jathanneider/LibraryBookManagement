package com.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection connection;

    public BookDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library_db", "postgres", "yourpassword");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> searchBooks(String query) {
        List<String> books = new ArrayList<>();
        try {
            String sql = "SELECT title, authors, average_rating FROM books WHERE title ILIKE ? OR authors ILIKE ? OR isbn ILIKE ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            stmt.setString(3, "%" + query + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add("Title: " + rs.getString("title") +
                        "\nAuthor(s): " + rs.getString("authors") +
                        "\nRating: " + rs.getDouble("average_rating") +
                        "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books.isEmpty() ? List.of("No results found.") : books;
    }

    public String getRandomBook() {
        String result = "No books found.";
        try {
            String sql = "SELECT title, authors, average_rating FROM books ORDER BY RANDOM() LIMIT 1";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = "Title: " + rs.getString("title") +
                        "\nAuthor(s): " + rs.getString("authors") +
                        "\nRating: " + rs.getDouble("average_rating");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
