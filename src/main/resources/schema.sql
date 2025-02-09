CREATE DATABASE library_db;

\c library_db;

CREATE TABLE books (
                       bookID SERIAL PRIMARY KEY,
                       title TEXT NOT NULL,
                       authors TEXT NOT NULL,
                       average_rating DECIMAL(3,2),
                       isbn VARCHAR(20) UNIQUE,
                       isbn13 VARCHAR(20) UNIQUE,
                       language_code VARCHAR(10),
                       num_pages INT,
                       ratings_count INT,
                       text_reviews_count INT
);