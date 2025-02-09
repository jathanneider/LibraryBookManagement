import kagglehub
import psycopg2
import csv
import os

# Download dataset
dataset_path = kagglehub.dataset_download("jealousleopard/goodreadsbooks")
csv_file_path = os.path.join(dataset_path, "books.csv")

# Database credentials
DB_NAME = "library_db"
DB_USER = "postgres"
DB_PASSWORD = "yourpassword"
DB_HOST = "localhost"
DB_PORT = "5432"

try:
    conn = psycopg2.connect(
        dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD, host=DB_HOST, port=DB_PORT
    )
    cursor = conn.cursor()
    print("Connected to the database.")

    # Open and read CSV
    with open(csv_file_path, "r", encoding="utf-8") as file:
        reader = csv.reader(file)
        next(reader)  # Skip header row

        for row in reader:
            try:
                # Validate and clean data
                title = row[1]
                authors = row[2]
                average_rating = float(row[3]) if row[3].replace(".", "", 1).isdigit() else None
                isbn = row[4]
                isbn13 = row[5]
                language_code = row[6][:20]  # Truncate to 20 characters
                num_pages = int(row[7]) if row[7].isdigit() else None
                ratings_count = int(row[8]) if row[8].isdigit() else None
                text_reviews_count = int(row[9]) if row[9].isdigit() else None

                # Insert into the database
                cursor.execute(
                    """
                    INSERT INTO books (title, authors, average_rating, isbn, isbn13, language_code, num_pages, ratings_count, text_reviews_count)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
                    ON CONFLICT (isbn) DO NOTHING;
                    """,
                    (title, authors, average_rating, isbn, isbn13, language_code, num_pages, ratings_count, text_reviews_count),
                )

            except Exception as e:
                conn.rollback()  # Rollback failed transaction
                print(f"Skipping row due to error: {row} - {e}")

    conn.commit()
    cursor.close()
    conn.close()
    print("Data import complete.")

except Exception as e:
    print("Error:", e)