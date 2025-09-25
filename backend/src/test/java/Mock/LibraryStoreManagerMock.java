package Mock;

import classes.*;
import mysql_connection.databaseconnection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static textinterface.textinterface_Main.currentDate;



public class LibraryStoreManagerMock {

    @Test
    public void testResetSuspensions() throws Exception {

        int memberId = 45;

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockSelectStatement = mock(PreparedStatement.class);
        PreparedStatement mockUpdateStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockSelectStatement)
                .thenReturn(mockUpdateStatement);
        when(mockSelectStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockUpdateStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt(anyString())).thenReturn(memberId);


        try (MockedStatic<databaseconnection> mockedStatic = mockStatic(databaseconnection.class)) {
            mockedStatic.when(databaseconnection::getConnection).thenReturn(mockConnection);

            CurrentDate mockCurrentDate = mock(CurrentDate.class);
            when(mockCurrentDate.getCurrentDate()).thenReturn(LocalDate.of(2022, 1, 1));


            LibraryStoreManager libraryStoreManager = new LibraryStoreManager(mockCurrentDate);

            libraryStoreManager.resetSuspensions();
            verify(mockSelectStatement, times(1)).setDate(1, java.sql.Date.valueOf(LocalDate.of(2022, 1, 1)));
            verify(mockUpdateStatement, atLeastOnce()).executeUpdate();

        }
    }


    @Test
    public void RemoveMemberTest() throws Exception {

        int memberId = 45;

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);


        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);


        try (MockedStatic<databaseconnection> mockedStatic = mockStatic(databaseconnection.class)) {
            mockedStatic.when(databaseconnection::getConnection).thenReturn(mockConnection);


            LibraryStoreManager libraryStoreManager = new LibraryStoreManager(mock(CurrentDate.class));


            libraryStoreManager.removeMember(memberId);


            verify(mockConnection, atLeastOnce()).prepareStatement(anyString());
            verify(mockStatement, atLeastOnce()).executeUpdate();
        }
    }

    @Test
    public void LendBookTest() throws Exception {
        // Setup
        int memberId = 45;
        long isbn = 9780307476463L;

        LibraryStoreManager libraryStoreManager = new LibraryStoreManager(mock(CurrentDate.class));


        Field lendingStoreField = LibraryStoreManager.class.getDeclaredField("lendingStore");
        lendingStoreField.setAccessible(true);

        LendingStore mockLendingStore = mock(LendingStore.class);
        when(mockLendingStore.LendBookStore(memberId, isbn)).thenReturn(1);

        lendingStoreField.set(libraryStoreManager, mockLendingStore);


        libraryStoreManager.lendBook(memberId, isbn);
        verify(mockLendingStore).LendBookStore(memberId, isbn);


    }


    @Test
    public void UpdateBookCopiesTest() throws Exception {
        long isbn = 9780307476463L;
        int newCopyCount = 4;


        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);


        when(mockConnection.prepareStatement(anyString())).thenReturn(mockUpdateStmt);
        when(mockUpdateStmt.executeUpdate()).thenReturn(1);


        try (MockedStatic<databaseconnection> mockedStatic = mockStatic(databaseconnection.class)) {
            mockedStatic.when(databaseconnection::getConnection).thenReturn(mockConnection);


            CurrentDate mockCurrentDate = mock(CurrentDate.class);
            when(mockCurrentDate.getCurrentDate()).thenReturn(LocalDate.now());


            LibraryStoreManager libraryStoreManager = new LibraryStoreManager(mockCurrentDate);


            libraryStoreManager.UpdateBookCopies(isbn, newCopyCount);


            verify(mockUpdateStmt).setInt(1, newCopyCount);
            verify(mockUpdateStmt).setLong(2, isbn);
            verify(mockUpdateStmt, times(1)).executeUpdate();
        }
    }

    @Test
    public void ReturnItemTest() throws Exception {
        int memberId = 1;
        long isbn = 97800002;

        LendingStore mockLendingStore = mock(LendingStore.class);
        when(mockLendingStore.returnItemStore(memberId, isbn)).thenReturn(true);

        CurrentDate mockCurrentDate = mock(CurrentDate.class);
        when(mockCurrentDate.getCurrentDate()).thenReturn(LocalDate.now());

        LibraryStoreManager libraryStoreManager = new LibraryStoreManager(mockCurrentDate);

        Field lendingStoreField = LibraryStoreManager.class.getDeclaredField("lendingStore");
        lendingStoreField.setAccessible(true);
        lendingStoreField.set(libraryStoreManager, mockLendingStore);

        libraryStoreManager.returnItem(memberId, isbn);
        verify(mockLendingStore).returnItemStore(memberId, isbn);
    }



    @Test
    public void PermabanMemberTest() throws Exception {
        int memberId = 45;

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);


        try (MockedStatic<databaseconnection> mockedStatic = mockStatic(databaseconnection.class)) {
            mockedStatic.when(databaseconnection::getConnection).thenReturn(mockConnection);

            CurrentDate mockCurrentDate = mock(CurrentDate.class);
            when(mockCurrentDate.getCurrentDate()).thenReturn(LocalDate.now());

            LibraryStoreManager libraryStoreManager = new LibraryStoreManager(mockCurrentDate);

            libraryStoreManager.permaBanMember(memberId);

            verify(mockStatement, atLeastOnce()).executeUpdate();

        }
    }
            // ALLLA DESSA BLIR TILL ADD MEMBER
        @Test
        public void checkUserExistsStoreTest() throws Exception {
            String socSecNr = "200004101234";
            boolean expectedUserExistsResult = true; // Adjust based on the scenario you want to test

            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(expectedUserExistsResult);

            try (MockedStatic<databaseconnection> mockedStatic = mockStatic(databaseconnection.class)) {
                mockedStatic.when(databaseconnection::getConnection).thenReturn(mockConnection);

                LibraryStoreManager libraryStoreManager = new LibraryStoreManager(mock(CurrentDate.class));

                boolean userExists = libraryStoreManager.checkUserExists(socSecNr);

                verify(mockPreparedStatement).setString(1, socSecNr);
                verify(mockPreparedStatement).executeQuery();
                verify(mockResultSet).next();

                assertTrue(userExists == expectedUserExistsResult);
            }
        }
    @Test
    public void checkUserBannedStoreTest() throws Exception {
        String socSecNr = "200004101234";
        String bannedUserLine = "200004101234";
        String filePath = "perma_banned_members.txt";

        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine()).thenReturn(bannedUserLine, (String) null);

        try (MockedConstruction<BufferedReader> mocked = mockConstruction(BufferedReader.class, (mock, context) -> {
            when(mock.readLine()).thenReturn(bannedUserLine, (String) null);
        })) {

            LibraryStoreManager libraryStoreManager = new LibraryStoreManager(mock(CurrentDate.class));
            boolean isBanned = libraryStoreManager.checkUserBanned(socSecNr);

            assertTrue(isBanned);


            mockBufferedReader.readLine();
            verify(mockBufferedReader, atLeastOnce()).readLine();
        }
    }

    @Test
    public void updateNullMemberStoreTest() throws Exception {
        Member mockMember = new Member(0, "John", "Doe", 1, "200004101234");
        int memberIdToUpdate = 10; // Example member ID to update

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockSelectStmt = mock(PreparedStatement.class);
        PreparedStatement mockUpdateStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockSelectStmt)
                .thenReturn(mockUpdateStmt);

        when(mockSelectStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("member_id")).thenReturn(memberIdToUpdate);

        when(mockUpdateStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<databaseconnection> mockedStatic = mockStatic(databaseconnection.class)) {
            mockedStatic.when(databaseconnection::getConnection).thenReturn(mockConnection);

            LibraryStoreManager libraryStoreManager = new LibraryStoreManager(mock(CurrentDate.class));
            boolean result = libraryStoreManager.updateNullMember(mockMember);

            assertTrue(result);
            verify(mockUpdateStmt).setString(1, mockMember.getFirstName());
            verify(mockUpdateStmt).setString(2, mockMember.getLastName());
            verify(mockUpdateStmt).setInt(3, mockMember.getMemberType());
            verify(mockUpdateStmt).setString(4, mockMember.getSocialSecNr());
            verify(mockUpdateStmt).setInt(5, memberIdToUpdate);
            verify(mockUpdateStmt).executeUpdate();
        }
    }

    @Test
    public void addNewMemberTest() throws Exception {
        Member mockMember = new Member(0, "John", "Doe", 1, "200004101234");

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockGetMaxIdStmt = mock(PreparedStatement.class);
        PreparedStatement mockInsertStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockGetMaxIdStmt).thenReturn(mockInsertStmt);
        when(mockGetMaxIdStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("max_id")).thenReturn(10);

        when(mockInsertStmt.executeUpdate()).thenReturn(1);

        try (MockedStatic<databaseconnection> mockedStatic = mockStatic(databaseconnection.class)) {
            mockedStatic.when(databaseconnection::getConnection).thenReturn(mockConnection);

            LibraryStoreManager libraryStoreManager = new LibraryStoreManager(mock(CurrentDate.class));
             int newId = libraryStoreManager.addNewMember(mockMember);

            assertEquals(11, newId);
            verify(mockInsertStmt).setInt(1, 11);
            verify(mockInsertStmt).setString(2, mockMember.getFirstName());
            verify(mockInsertStmt).setString(3, mockMember.getLastName());
            verify(mockInsertStmt).setInt(4, mockMember.getMemberType());
            verify(mockInsertStmt).setString(5, mockMember.getSocialSecNr());
            verify(mockInsertStmt).executeUpdate();
        }
    }
    }






