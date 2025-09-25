package mysql_connection;

public class setup_database {
    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        System.out.println("Database initialized successfully!");
    }
}