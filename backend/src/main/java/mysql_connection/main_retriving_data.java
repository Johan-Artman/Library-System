package mysql_connection;

import classes.Member;
import interfaces.MemberType;

import java.sql.*;
import java.util.Optional;

//TODO: Testfunktion!, allt funkar.
public class main_retriving_data {
  public static void getBooks() {
    String query = "SELECT titel, isbn FROM books;";
    try (Connection connection = databaseconnection.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {

      while (resultSet.next()) {
        String titel = resultSet.getString("titel");
        long isbn = resultSet.getLong("isbn");
        System.out.println("Titel: " + titel + ".  isbn: " + isbn  );
      }
    } catch (SQLException e) {
      System.err.println("Query error: " + e.getMessage());
    }
  }



  public static void main(String[] args) {
    // Initialize database with tables and sample data
    DatabaseInitializer.initializeDatabase();
    getBooks();
  }
  }
