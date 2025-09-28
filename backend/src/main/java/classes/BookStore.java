package classes;

import interfaces.IBook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class BookStore {
    private static final Logger logger = LogManager.getLogger("buggaren");
    private Connection connection;

    public BookStore(Connection connection, CurrentDate currentDate) {
        this.connection = connection;

    }

        public Optional<Book> getBookByISBNStore(long isbn) {
            logger.info("Entering getBookByISBNStore with ISBN: {}", isbn);

            String queryisbn = "SELECT isbn, titel, availableCopies FROM books WHERE isbn = ?;";

            try (PreparedStatement statementisbn = connection.prepareStatement(queryisbn)) {
                statementisbn.setLong(1, isbn);
                ResultSet resultSet = statementisbn.executeQuery();
                if (resultSet.next()) {
                    String title = resultSet.getString("titel");
                    int availableCopies = resultSet.getInt("availableCopies");
                    Book booktologger = new Book(isbn, title, availableCopies);
                    logger.info("Exiting getBookByISBNStore with result: {}", booktologger);
                    return Optional.of(new Book(isbn, title, availableCopies));
                }
            } catch (SQLException e) {
                logger.error("SQL exception in getBookByISBNStore", e);
                throw new RuntimeException();
            }
            logger.info("Exiting getBookByISBNStore with no result for ISBN: {}", isbn);
            return Optional.empty();
        }

        public java.util.List<Book> getAllBooksStore() {
            logger.info("Entering getAllBooksStore");
            java.util.List<Book> books = new java.util.ArrayList<>();
            String getAllBooksQuery = "SELECT isbn, titel, availableCopies FROM books;";
            
            try (PreparedStatement statement = connection.prepareStatement(getAllBooksQuery)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    long isbn = resultSet.getLong("isbn");
                    String title = resultSet.getString("titel");
                    int availableCopies = resultSet.getInt("availableCopies");
                    books.add(new Book(isbn, title, availableCopies));
                }
                logger.info("Exiting getAllBooksStore with {} books found", books.size());
            } catch (SQLException e) {
                logger.error("SQL exception in getAllBooksStore", e);
                throw new RuntimeException("Error retrieving all books", e);
            }
            
            return books;
        }

        public java.util.List<Book> searchBooksByTitle(String title) {
            logger.info("Entering searchBooksByTitle with title: {}", title);
            java.util.List<Book> books = new java.util.ArrayList<>();
            String searchQuery = "SELECT isbn, titel, availableCopies FROM books WHERE LOWER(titel) LIKE LOWER(?);";
            
            try (PreparedStatement statement = connection.prepareStatement(searchQuery)) {
                statement.setString(1, "%" + title + "%");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    long isbn = resultSet.getLong("isbn");
                    String bookTitle = resultSet.getString("titel");
                    int availableCopies = resultSet.getInt("availableCopies");
                    books.add(new Book(isbn, bookTitle, availableCopies));
                }
                logger.info("Exiting searchBooksByTitle with {} books found", books.size());
            } catch (SQLException e) {
                logger.error("SQL exception in searchBooksByTitle", e);
                throw new RuntimeException("Error searching books by title", e);
            }
            
            return books;
        }

        public void addBookStore(IBook book) {
            logger.info("Entering addBookStore with IBook: {}", book);
            String addbookquery = "INSERT INTO books (isbn, titel, antal) VALUES (?, ?, ?);";
            try (PreparedStatement statement = connection.prepareStatement(addbookquery)) {
                statement.setLong(1, book.getISBN());
                statement.setString(2, book.getTitle());
                statement.setInt(3, book.getAvailableCopies());
                statement.executeUpdate();
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    logger.info("Book with ISBN {} was added successfully.", book.getISBN());
                } else {
                    logger.info("No book was added for ISBN {}.", book.getISBN());
                }
            } catch (SQLException e) {
                logger.error("An error has occurred while adding a book with ISBN {}: {}", book.getISBN(), e.getMessage(), e);
            }

            logger.info("Exiting addBookStore for IBook: {}", book);
        }
    }

