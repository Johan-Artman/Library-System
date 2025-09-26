package classes;

import exceptions.MemberBannedException;
import exceptions.MemberExistsException;
import interfaces.ILibraryMember;
import interfaces.MemberType;
import mysql_connection.databaseconnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.parseLong;

public class MemberStore {
    private static final Logger logger = LogManager.getLogger("buggaren");
    private Connection connection;
    private CurrentDate currentDate;

    public MemberStore(Connection connection, CurrentDate currentDate) {
        this.connection = connection;
        this.currentDate = currentDate; // Assign the passed currentDate
    }

    public List<Member> getAllMembersStore() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Member member = new Member(
                    resultSet.getInt("id"),
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getInt("member_type"),
                    resultSet.getString("social_security_number")
                );
                members.add(member);
            }
        }
        return members;
    }


    public void resetSuspensionsStore(CurrentDate currentDate) {
        LocalDate today = currentDate.getCurrentDate();
        String selectSuspendedMembersQuery = "SELECT member_id FROM member_status WHERE dateend < ? AND isSuspended = 1";
        String updateSuspensionStatusQuery = "UPDATE member_status SET isSuspended = 0, datestart = NULL, dateend = NULL, suspend_count = 0 WHERE member_id = ?";

        List<Integer> memberIds = new ArrayList<>();

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSuspendedMembersQuery)) {
            selectStmt.setDate(1, Date.valueOf(today));
            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                memberIds.add(rs.getInt("member_id"));
            }
        } catch (SQLException e) {
            logger.error("Error selecting suspensions: {}", e.getMessage(), e);
        }
        if (!memberIds.isEmpty()) {
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSuspensionStatusQuery)) {
                for (Integer memberId : memberIds) {
                    updateStmt.setInt(1, memberId);
                    updateStmt.executeUpdate();
                    logger.info("Suspension reset for member ID: {}", memberId);
                }

            } catch (SQLException e) {
                logger.error("Error updating suspensions: {}", e.getMessage(), e);
            }
        } else {
            logger.info("No suspensions to reset.");
        }
    }


    public Optional<Member> getMemberByIdStore(int memberId) {
        String query = "SELECT * FROM member WHERE member_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, memberId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("member_id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                int memberType = resultSet.getInt("member_type");
                String socialSecNr = resultSet.getString("soc_sec_nr");
                Member member = new Member(id, firstName, lastName, memberType, socialSecNr);
                return Optional.of(member);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving member by ID: {}. {}", memberId, e.getMessage(), e);
        }
        return Optional.empty(); // Return an empty Optional if the member is not found
    }

    public int addMemberStore(Member member) throws MemberExistsException, MemberBannedException {
        try {
            if (checkUserExistsStore(member.getSocialSecNr())) {
                throw new MemberExistsException("User already exists with Social Security Number: " + member.getSocialSecNr());
            }

            if (checkUserBannedStore(member.getSocialSecNr())) {
                throw new MemberBannedException("This user is permanently banned. Social Security Number: " + member.getSocialSecNr());
            }

            if (!updateNullMemberStore(member)) {
                return addNewMemberStore(member); // Return the new member ID
            }
        } catch (SQLException e) {
            logger.error("Database error while adding member: {}", e.getMessage(), e);
            throw new RuntimeException("Database error while adding member", e);
        }
        return -1;
    }

    public boolean checkUserExistsStore(String socSecNr) throws SQLException {
        String query = "SELECT 1 FROM member WHERE soc_sec_nr = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, socSecNr);
            ResultSet rs = stmt.executeQuery();
            boolean userExists = rs.next();
            if (userExists) {
                logger.debug("User exists with Social Security Number: {}", socSecNr);
            } else {
                logger.debug("No existing user found with Social Security Number: {}", socSecNr);
            }
            return userExists;
        } catch (SQLException e) {
            logger.error("Error checking if user exists for Social Security Number: {}. {}", socSecNr, e.getMessage(), e);
            throw e;
        }

    }


    public boolean checkUserBannedStore(String socSecNr) throws SQLException {
        String filePath = "perma_banned_members.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {

                String currentNumber = String.valueOf(parseLong(currentLine.trim()));
                if (currentLine.trim().equals(socSecNr)) {
                    logger.info("User with Social Security Number {} is banned.", socSecNr);
                    return true; // The number is found in the file
                }
            }
        } catch (IOException e) {
            logger.error("Error reading from file: {}", e.getMessage(), e);
        } catch (NumberFormatException e) {
            logger.error("Error parsing number from file: {}", e.getMessage(), e);
        }
        return false; // The number was not found in the file
    }

    public boolean updateNullMemberStore(Member member) throws SQLException {
        String query = "SELECT member_id FROM member WHERE firstname IS NULL OR lastname IS NULL OR soc_sec_nr IS NULL LIMIT 1;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int memberIdToUpdate = rs.getInt("member_id");
                String updateQuery = "UPDATE member SET firstname = ?, lastname = ?, member_type = ?, soc_sec_nr = ? WHERE member_id = ?;";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, member.getFirstName());
                    updateStmt.setString(2, member.getLastName());
                    updateStmt.setInt(3, member.getMemberType());
                    updateStmt.setString(4, member.getSocialSecNr());
                    updateStmt.setInt(5, memberIdToUpdate);
                    updateStmt.executeUpdate();
                    System.out.println("Welcome " + member.getFirstName() + ", your id is: " + memberIdToUpdate);
                    return true;
                }
            }
            return false;
        }
    }

    public int addNewMemberStore(Member member) throws SQLException {
        logger.info("Attempting to add new member with Social Security Number: {}", member.getSocialSecNr());
        String getMaxIdQuery = "SELECT MAX(member_id) AS max_id FROM member;";
        int newId = 1;
        try (PreparedStatement getMaxIdStmt = connection.prepareStatement(getMaxIdQuery)) {
            ResultSet rs = getMaxIdStmt.executeQuery();
            if (rs.next()) {
                newId = rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            logger.error("Error obtaining the maximum member ID from database: {}", e.getMessage(), e);
            throw e;
        }

        String insertQuery = "INSERT INTO member (member_id, firstname, lastname, member_type, soc_sec_nr) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setInt(1, newId);
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getLastName());
            stmt.setInt(4, member.getMemberType());
            stmt.setString(5, member.getSocialSecNr());
            stmt.executeUpdate();
            logger.info("New member added: {} {}, ID: {}", member.getFirstName(), member.getLastName(), newId);
            return newId; // Return the new member ID
        } catch (SQLException e) {
            logger.error("Error adding new member to the database: {}", e.getMessage(), e);
            throw e;
        }
    }




    public void removeMemberStore(int memberId) {

        String anonymizeQuery = "UPDATE member SET firstname = NULL, lastname = NULL, member_type = NULL, soc_sec_nr = NULL WHERE member_id = ?;";
        String removeStatusQuery = "DELETE FROM member_status WHERE member_id = ?;";

        try (Connection connection = databaseconnection.getConnection();
             PreparedStatement anonymizeStmt = connection.prepareStatement(anonymizeQuery);
             PreparedStatement removeStatusStmt = connection.prepareStatement(removeStatusQuery)) {

            // Anonymize member information
            anonymizeStmt.setInt(1, memberId);
            int affectedRows = anonymizeStmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Member with ID: {} has been anonymized.", memberId);
            } else {
                logger.warn("No member found with ID: {}, or no update made.", memberId);
            }

            // Remove member's status information
            removeStatusStmt.setInt(1, memberId);
            affectedRows = removeStatusStmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Member status removed for member ID: {}", memberId);
            } else {
                logger.warn("No member status found or removed for member ID: {}", memberId);
            }
        } catch (SQLException e) {
            logger.error("Error during removal process for member with ID: {}: {}", memberId, e.getMessage(), e);

        }
    }

    public boolean permaBanMemberStore(int memberId) throws SQLException, IOException {
        String getMemberDetailsQuery = "SELECT soc_sec_nr, member_id FROM member WHERE member_id = ?;";

        String socialSecNr;

        try (Connection connection = databaseconnection.getConnection();
             PreparedStatement getDetailsStmt = connection.prepareStatement(getMemberDetailsQuery)) {

            getDetailsStmt.setInt(1, memberId);
            ResultSet rs = getDetailsStmt.executeQuery();

            if (rs.next()) {
                socialSecNr = String.valueOf(rs.getLong("soc_sec_nr"));

                // Write social security number to a text file
                writeSocialSecNumberToFile(socialSecNr);
                removeMemberStore(memberId);
                logger.info("Member with ID: {} permanently banned.", memberId);
                return true;

            } else {
                logger.warn("Member with ID: {} not found.", memberId);
                throw new SQLException("Member with ID: " + memberId + " not found.");

            }

        } catch (SQLException e) {
            logger.error("Error retrieving member details for ID: {}: {}", memberId, e.getMessage(), e);
            throw e; // Rethrow to handle or log at a higher level
        } catch (IOException e) {
            logger.error("Error writing to ban file for member ID: {}: {}", memberId, e.getMessage(), e);
            throw e; // Rethrow to handle or log at a higher level
        }
    }

    private void writeSocialSecNumberToFile(String socialSecNr) throws IOException {
        // Define the path to the text file
        String filePath = "perma_banned_members.txt";

        // Use try-with-resources to ensure the writer is closed properly
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Write the social security number to the file
            writer.write(socialSecNr);
            writer.newLine(); // Write a newline character after the number
            logger.info("Social security number written to file: {}", socialSecNr);
        } catch (IOException e) {
            logger.error("Error writing social security number to file: {}", e.getMessage(), e);
            throw e;
        }
    }




    public boolean autoSuspendMemberStore(int memberId) {
        try {
            int lateBooksCount = getLateBooksCount(memberId);

            // Check if a status record exists for the member and create one if not
            if (!memberStatusExists(memberId)) {
                initializeMemberStatus(memberId);
            }

            // Update the count of late books
            updateLateBookCount(memberId, lateBooksCount);

            if (lateBooksCount > 2) {
                // If the member has more than 2 late books, increment suspend count and apply suspension
                boolean suspensionApplied = applySuspension(memberId);
                if (suspensionApplied) {
                    logger.info("Suspension applied to member ID: {} due to late book returns.", memberId);
                }
                return suspensionApplied;
            } else {
                logger.debug("No action required. The member ID: {} has fewer than 3 late books.", memberId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Database error while attempting to auto-suspend member ID: {}: {}", memberId, e.getMessage(), e);
            return false;
        }
    }

    private int getLateBooksCount(int memberId) throws SQLException {
        LocalDate today = currentDate.getCurrentDate(); // Get the current date from the system or user input
        String query = "SELECT COUNT(*) AS late_count FROM borrowing_table WHERE member_id = ? AND utlanad = 1 AND DATE_ADD(lane_datum, INTERVAL 1 MONTH) < ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            stmt.setDate(2, java.sql.Date.valueOf(today));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("late_count");
            }
        }
        return 0;
    }


    private boolean applySuspension(int memberId) throws SQLException {
        logger.info("Attempting to apply suspension to member ID: {}", memberId);

        // Increment suspend_count and mark as suspended if not already suspended
        String updateQuery = "UPDATE member_status SET suspend_count = suspend_count + 1, " +
                "isSuspended = TRUE, datestart = ?, dateend = ? WHERE member_id = ?";
        LocalDate today = currentDate.getCurrentDate();
        LocalDate endDate = today.plusDays(15);

        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            updateStmt.setDate(1, java.sql.Date.valueOf(today));
            updateStmt.setDate(2, java.sql.Date.valueOf(endDate));
            updateStmt.setInt(3, memberId);
            updateStmt.executeUpdate();
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                logger.info("Suspension successfully applied to member ID: {}. Suspension period ends on {}", memberId, endDate);
                return true;
            } else {
                logger.warn("Failed to apply suspension: No member_status record found for member ID: {}", memberId);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error applying suspension to member ID: {}: {}", memberId, e.getMessage(), e);
            throw e;
        }
    }

    private boolean memberStatusExists(int memberId) throws SQLException {
        String query = "SELECT COUNT(1) FROM member_status WHERE member_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    private void initializeMemberStatus(int memberId) throws SQLException {
        logger.debug("Initializing status for member ID: {}", memberId);
        String insertStatusQuery = "INSERT INTO member_status (member_id, late_book_count, suspend_count, isSuspended) VALUES (?, 0, 0, FALSE)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertStatusQuery)) {
            insertStmt.setInt(1, memberId);
            insertStmt.executeUpdate();
            int rowsUpdated = insertStmt.executeUpdate();

            if (rowsUpdated > 0) {
                logger.info("Member status initialized for member ID: {}", memberId);
            } else {
                logger.warn("Member status initialization failed for member ID: {}", memberId);
            }
        } catch (SQLException e) {
            logger.error("Error initializing member status for member ID: {}: {}", memberId, e.getMessage(), e);
            throw e;
        }
    }


    private void updateLateBookCount(int memberId, int newCount) throws SQLException {
        logger.debug("Updating late book count for member ID: {} to {}", memberId, newCount);

        String updateQuery = "UPDATE member_status SET late_book_count = ? WHERE member_id = ?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            updateStmt.setInt(1, newCount);
            updateStmt.setInt(2, memberId);
            updateStmt.executeUpdate();
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                logger.info("Late book count updated successfully for member ID: {}. New count: {}", memberId, newCount);
            } else {

                logger.warn("No update made for member ID: {}. Member may not exist or already has the specified late book count.", memberId);
            }
        } catch (SQLException e) {
            logger.error("Error updating late book count for member ID: {}: {}", memberId, e.getMessage(), e);
            throw e;
        }
    }


    public boolean isSuspendedMemberStore(int memberId) {
        String query = "SELECT isSuspended FROM member_status WHERE member_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, memberId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int isSuspended = resultSet.getInt("isSuspended");
                return isSuspended == 1; // Return true if isSuspended is 1, otherwise false
            } else {
                logger.info("No suspension record found for member ID: {}", memberId);
                return false; // If no record is found, consider the member not suspended
            }
        } catch (SQLException e) {
            logger.error("Query error while checking suspension status for member ID: {}: {}", memberId, e.getMessage(), e);
            return false; // In case of SQL error, default to not suspended
        }
    }
/*
    public List<String> getAllBorrowedBooks(int memberId) throws SQLException {
        List<String> borrowedBookDetails = new ArrayList<>();
        String query = "SELECT books.isbn, books.titel FROM borrowing_table " +
                "JOIN books ON borrowing_table.isbn = books.isbn " +
                "WHERE borrowing_table.member_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long isbn = rs.getLong("isbn");
                String titel = rs.getString("titel");
                borrowedBookDetails.add("ISBN: " + isbn + ", Titel: " + titel);
            }
        }
        return borrowedBookDetails;
    }
*/

}
