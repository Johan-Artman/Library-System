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
      String url = "jdbc:sqlite:library.db"; // SQLite database file
      connection = DriverManager.getConnection(url);
      log.info("Connected to the SQLite database!");
    } catch (SQLException e) {
      log.error("Connection error: " + e.getMessage());
    }
    return connection;
  }
}
