package com.library;

import java.sql.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class DatabaseManager {
    private static final String DB_NAME = "library_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "yourpassword";  // Replace with your actual password
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "5432";
    private static final String DB_URL = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/";

    private static Connection connection;

    public static void initializeDatabase() {
        try {
            Connection defaultConnection = DriverManager.getConnection(DB_URL + "postgres", DB_USER, DB_PASSWORD);
            Statement stmt = defaultConnection.createStatement();

            // Ensure the database exists
            ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + DB_NAME + "'");
            if (!rs.next()) {
                System.out.println("Database not found. Creating `" + DB_NAME + "`...");
                stmt.executeUpdate("CREATE DATABASE " + DB_NAME);
                System.out.println("Database `" + DB_NAME + "` created successfully.");
            }
            stmt.close();
            defaultConnection.close();

            connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
            System.out.println("Connected to `" + DB_NAME + "`.");

            initializeTables(); // Ensure tables exist before running the import

            // Run Python import script only if data is missing
            if (!isDataPresent()) {
                System.out.println("No data found. Running `data_import.py`...");
                installPythonDependencies();

                ProcessBuilder pb = new ProcessBuilder("python3", "src/main/resources/data_import.py");
                pb.redirectErrorStream(true);
                Process process = pb.start();
                process.waitFor();

                System.out.println("Data import completed successfully.");
            } else {
                System.out.println("Books table already contains data.");
            }

        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Ensure tables exist before importing
    private static void initializeTables() {
        try (Statement stmt = connection.createStatement()) {
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS books (
                    bookID SERIAL PRIMARY KEY,
                    title TEXT NOT NULL,
                    authors TEXT NOT NULL,
                    average_rating FLOAT,
                    isbn VARCHAR(20) UNIQUE,
                    isbn13 VARCHAR(20),
                    language_code VARCHAR(20),
                    num_pages INT,
                    ratings_count INT,
                    text_reviews_count INT
                )
            """;
            stmt.executeUpdate(createTableSQL);
            System.out.println("Checked table `books`.");
        } catch (Exception e) {
            System.err.println("Error ensuring tables exist: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Check if data is already in the table
    private static boolean isDataPresent() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM books")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error checking for existing data: " + e.getMessage());
        }
        return false;
    }

    // Ensure Python dependencies are installed
    private static void installPythonDependencies() {
        try {
            System.out.println("Checking and installing required Python packages...");

            ProcessBuilder pb = new ProcessBuilder("python3", "-m", "pip", "install", "--user", "-r", "src/main/resources/requirements.txt");
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // Print the installation output
            }

            process.waitFor();
            System.out.println("Python dependencies installed successfully.");
        } catch (Exception e) {
            System.err.println("Error installing Python dependencies: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}