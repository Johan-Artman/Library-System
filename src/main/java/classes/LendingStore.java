package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LendingStore {
    private static final Logger logger = LogManager.getLogger("buggaren");
    private final Connection connection;
    private final BookStore bookStore;
    private final CurrentDate currentDate;

    public LendingStore(Connection connection, BookStore bookStore, CurrentDate currentDate) {
        this.connection = connection;
        this.bookStore = bookStore;
        this.currentDate = currentDate;
    }

    public int LendBookStore(int memberId, long isbn) {
        logger.info("Attempting to lend book with ISBN: {} to member with ID: {}", isbn, memberId);
        int susscesfullylent = 0;
        try {
            String memberQuery = "SELECT soc_sec_nr, member_type FROM member WHERE member_id = ?";
            try (PreparedStatement memberStatement = connection.prepareStatement(memberQuery)) {
                memberStatement.setInt(1, memberId);
                ResultSet MemberResultSet = memberStatement.executeQuery();

                if (MemberResultSet.next() && MemberResultSet.getString("soc_sec_nr") != null) {
                    int memberType = MemberResultSet.getInt("member_type");
                    int maxBooksAllowed = getMaxBooksAllowed(memberType);

                    String countQuery = "SELECT COUNT(*) AS book_count FROM borrowing_table WHERE member_id = ? AND utlanad = TRUE";
                    try (PreparedStatement countStmt = connection.prepareStatement(countQuery)) {
                        countStmt.setInt(1, memberId);
                        ResultSet countRs = countStmt.executeQuery();

                        if (countRs.next()) {
                            int currentBooksLent = countRs.getInt("book_count");

                            if (currentBooksLent < maxBooksAllowed) {
                                Optional<Book> bookOptional = bookStore.getBookByISBNStore(isbn);
                                if (bookOptional.isPresent() && bookOptional.get().getAvailableCopies() > 0) {
                                    int lendstatus = lendBook(memberId, isbn);
                                    if (lendstatus == 1) {
                                        logger.info("Member with ID: {} has lent book {}", memberId, isbn);
                                        susscesfullylent = 1;
                                    }

                                } else {
                                    susscesfullylent = 3;
                                    logger.info("book no available //lending store ");
                                }
                            } else {
                                logger.info("Member with ID: {} cannot lend more books. Limit reached.", memberId);
                                susscesfullylent = 2;
                            }
                        }
                    }
                } else {
                    logger.info("Member with ID: {} cannot lend books due to null social security number or does not exist.", memberId);
                }
            }
        } catch (SQLException e) {
            logger.error("Database error while processing lending for Member ID: {}: {}", memberId, e.getMessage(), e);
        }
        return susscesfullylent;
    }

    private int getMaxBooksAllowed(int memberType) {
        return switch (memberType) {
            case 1 -> 3;
            case 2 -> 5;
            case 3 -> 7;
            case 4 -> 10;
            default -> 0;
        };
    }

    private int lendBook(int memberId, long isbn) {
        AtomicInteger susscesfullylent = new AtomicInteger();
        Optional<Book> bookOptional = bookStore.getBookByISBNStore(isbn);
        bookOptional.ifPresent(book -> {
            if (book.getAvailableCopies() > 0) {
                UpdatebookcopiesStore(isbn, book.getAvailableCopies() - 1);

                String insertQuery = "INSERT INTO borrowing_table (transaction_id, member_id, isbn, lane_datum, utlanad) VALUES (DEFAULT, ?, ?, ?, TRUE);";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    LocalDate today = currentDate.getCurrentDate();
                    String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    insertStmt.setInt(1, memberId);
                    insertStmt.setLong(2, isbn);
                    insertStmt.setString(3, formattedDate);
                    insertStmt.executeUpdate();
                    susscesfullylent.set(1);
                    logger.info("Book with ISBN: {} has been successfully lent out.", isbn);
                } catch (SQLException e) {
                    logger.error("Error during book lending process for ISBN: {}: {}", isbn, e.getMessage(), e);
                }
            } else {
                logger.info("Book with ISBN {} has no available copies to lend.", isbn);
                susscesfullylent.set(3);
            }
        });
        return susscesfullylent.get();
    }

    public void UpdatebookcopiesStore(long isbn, int newCopyCount) {
        logger.info("Attempting to update the copies for ISBN : {}", isbn);
        String query = "UPDATE books SET availableCopies = ? WHERE isbn = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newCopyCount);
            statement.setLong(2, isbn);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("The available copies for ISBN: {} updated successfully to {}", isbn, newCopyCount);
            } else {
                logger.warn("No records found to update for ISBN: {}", isbn);
            }

        } catch (SQLException e) {
            logger.error("Error updating book copies for isbn: {}", isbn, e);
        }
    }

    //gjort om så den returnerar bool ist så vi kan printa hur det gick i managern ist
    public boolean returnItemStore(int memberId, long isbn) {
        logger.info("Attempting to process return for book with ISBN: {} by member ID: {}", isbn, memberId);

        boolean isSuccess = false;
        try {

            PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE borrowing_table SET utlanad = FALSE WHERE member_id = ? AND isbn = ?;");
            updateStatement.setInt(1, memberId);
            updateStatement.setLong(2, isbn);


            int affectedRows = updateStatement.executeUpdate();

            if (affectedRows > 0) {
                Optional<Book> bookOptional = bookStore.getBookByISBNStore(isbn);
                if (bookOptional.isPresent()) {
                    Book book = bookOptional.get();
                    logger.info("Book with ISBN: {} successfully returned by member ID: {}", isbn, memberId);
                    UpdatebookcopiesStore(isbn, book.getAvailableCopies() + 1);
                    isSuccess = true;
                }
            } else {
                logger.info("No borrowing record found for book with ISBN: {} and member ID: {}", isbn, memberId);


            }
        } catch (SQLException e) {
            logger.error("Error updating borrowing record for book with ISBN: {} and member ID: {}", isbn, memberId, e);
        }
        return isSuccess;
    }
}
