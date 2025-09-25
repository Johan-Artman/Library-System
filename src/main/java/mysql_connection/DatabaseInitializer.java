package mysql_connection;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseInitializer {
    private static final Logger log = LogManager.getLogger("DatabaseInitializer");
    
    public static void initializeDatabase() {
        try (Connection connection = databaseconnection.getConnection();
             Statement statement = connection.createStatement()) {
            
            // Create member_types table first (needed for foreign key)
            String createMemberTypesTable = "CREATE TABLE IF NOT EXISTS member_types (" +
                "id INTEGER PRIMARY KEY, " +
                "type_name TEXT NOT NULL UNIQUE" +
                ");";
            statement.execute(createMemberTypesTable);
            log.info("Member types table created or already exists");
            
            // Create books table
            String createBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titel TEXT NOT NULL, " +
                "isbn INTEGER NOT NULL UNIQUE, " +
                "availableCopies INTEGER DEFAULT 1" +
                ");";
            statement.execute(createBooksTable);
            log.info("Books table created or already exists");
            
            // Create member table
            String createMemberTable = "CREATE TABLE IF NOT EXISTS member (" +
                "member_id INTEGER PRIMARY KEY, " +
                "firstname TEXT, " +
                "lastname TEXT, " +
                "member_type INTEGER, " +
                "soc_sec_nr TEXT, " +
                "FOREIGN KEY (member_type) REFERENCES member_types(id)" +
                ");";
            statement.execute(createMemberTable);
            log.info("Member table created or already exists");
            
            // Create member_status table
            String createMemberStatusTable = "CREATE TABLE IF NOT EXISTS member_status (" +
                "member_id INTEGER PRIMARY KEY, " +
                "late_book_count INTEGER DEFAULT 0, " +
                "suspend_count INTEGER DEFAULT 0, " +
                "isSuspended INTEGER DEFAULT 0, " +
                "datestart DATE, " +
                "dateend DATE, " +
                "FOREIGN KEY (member_id) REFERENCES member(member_id)" +
                ");";
            statement.execute(createMemberStatusTable);
            log.info("Member status table created or already exists");
            
            // Create borrowing_table
            String createBorrowingTable = "CREATE TABLE IF NOT EXISTS borrowing_table (" +
                "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "member_id INTEGER, " +
                "isbn INTEGER, " +
                "lane_datum DATE, " +
                "utlanad INTEGER DEFAULT 1, " +
                "FOREIGN KEY (member_id) REFERENCES member(member_id), " +
                "FOREIGN KEY (isbn) REFERENCES books(isbn)" +
                ");";
            statement.execute(createBorrowingTable);
            log.info("Borrowing table created or already exists");
            
            // Insert member types first (needed for foreign key)
            String insertMemberTypes = "INSERT OR IGNORE INTO member_types (id, type_name) VALUES " +
                "(1, 'UNDERGRADUATE'), " +
                "(2, 'POSTGRADUATE'), " +
                "(3, 'PHD'), " +
                "(4, 'TEACHER');";
            statement.execute(insertMemberTypes);
            log.info("Sample member types inserted");
            
            // Insert sample books
            String insertSampleBooks = "INSERT OR IGNORE INTO books (titel, isbn, availableCopies) VALUES " +
                "('The Great Gatsby', 9780743273565, 3), " +
                "('To Kill a Mockingbird', 9780061120084, 2), " +
                "('1984', 9780451524935, 4), " +
                "('Pride and Prejudice', 9780141439518, 2), " +
                "('The Catcher in the Rye', 9780316769174, 1), " +
                "('Dune', 9780441172719, 2), " +
                "('The Lord of the Rings', 9780544003415, 3), " +
                "('Harry Potter and the Sorcerers Stone', 9780439708180, 5);";
            statement.execute(insertSampleBooks);
            log.info("Sample books inserted");
            
            // Insert sample members
            String insertSampleMembers = "INSERT OR IGNORE INTO member (member_id, firstname, lastname, member_type, soc_sec_nr) VALUES " +
                "(1, 'John', 'Doe', 1, '199001011234'), " +
                "(2, 'Jane', 'Smith', 2, '199502152345'), " +
                "(3, 'Bob', 'Johnson', 3, '199803203456'), " +
                "(4, 'Alice', 'Brown', 4, '198512054567'), " +
                "(5, 'Charlie', 'Davis', 1, '200006106789');";
            statement.execute(insertSampleMembers);
            log.info("Sample members inserted");
            
            // Initialize member status for sample members
            String insertSampleMemberStatus = "INSERT OR IGNORE INTO member_status (member_id) VALUES " +
                "(1), (2), (3), (4), (5);";
            statement.execute(insertSampleMemberStatus);
            log.info("Sample member status initialized");
            
        } catch (SQLException e) {
            log.error("Database initialization error: " + e.getMessage());
        }
    }
}