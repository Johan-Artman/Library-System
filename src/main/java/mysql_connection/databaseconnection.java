package mysql_connection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


// TODO: ALLT FUNKAR SOM DET SKA
public class databaseconnection {
  private static final Logger log = LogManager.getLogger("buggaren");
  public static Connection getConnection() {
    Connection connection = null;
    try {
      String url = "jdbc:mysql://localhost:3304/bibliotek";
      String username = "dbconnect";
      String password = "Pineapple123.";
      connection = DriverManager.getConnection(url, username, password);
      log.info("Connected to the database!");
    } catch (SQLException e) {
      log.error("Connection error: " + e.getMessage());
    }
    return connection;
  }
}
