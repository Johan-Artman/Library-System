package DataConnectiontTest;

import mysql_connection.databaseconnection;

import java.sql.Connection;

public class DatabaseConnectionTest {

  public static void main(String[] args) {
    Connection connection = null;
    try {
      connection = databaseconnection.getConnection();
      if (connection != null) {
        System.out.println("Connection to the database was successful!");
      } else {
        System.out.println("Failed to make connection!");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
