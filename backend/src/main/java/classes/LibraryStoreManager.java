package classes;

import exceptions.MemberBannedException;
import exceptions.MemberExistsException;
import interfaces.IBook;
import interfaces.ILibraryMember;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import mysql_connection.databaseconnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// data kommer i formen av.
// member_status
//member_id int ,
// status bigint,
// datestart date,
// dateend date.
//borrowing_table
//transaction_id bigint auto_increment not null primary key,
//member_id int,
//isbn bigint.
//Member (
//member_id int not null primary key, name char(255),
//lastname char (255),
//member_type int ).
//create table books(
//isbn bigint not null primary key, titel char )
public class LibraryStoreManager {
    private static final Logger logger = LogManager.getLogger("buggaren");

    private Map<Integer, ILibraryMember> members;
    private Map<Long, IBook> books;
    private MemberStore memberStore;
    private BookStore bookStore;
    private LendingStore lendingStore;

    private CurrentDate currentDate;


    public LibraryStoreManager(CurrentDate currentDate) {
        Connection connection = databaseconnection.getConnection();
        this.members = new HashMap<>();
        this.books = new HashMap<>();
        this.currentDate = currentDate;
        this.memberStore = new MemberStore(connection, currentDate);
        this.bookStore = new BookStore(connection, currentDate);
        this.lendingStore = new LendingStore(connection, bookStore, currentDate);
    }


    public void addMember(Member member) throws MemberBannedException, MemberExistsException {
        int newMemberId = -1;
        try {
            newMemberId = memberStore.addMemberStore(member);
            if (newMemberId != -1) {
                System.out.println("Welcome " + member.getFirstName() + ", your registration is successful. Your id is " + newMemberId);
            } else {

            }
        } catch (MemberExistsException e) {
            System.out.println(e.getMessage());
        } catch (MemberBannedException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void removeMember(int memberId) {
        try {
            memberStore.removeMemberStore(memberId);

            System.out.println("Member removal process completed for ID: " + memberId);
        } catch (RuntimeException e) {
            logger.error("Failed to remove member with ID: {}: {}", memberId, e.getMessage(), e);

            System.err.println("An error occurred while processing the removal for member ID: " + memberId + ". Please try again.");
        }
    }

    public void addBook(IBook book) {
        bookStore.addBookStore(book);
    }

    public Optional<Book> getBookByISBN(long isbn) {

        System.out.println("Request to get book by ISBN: " + isbn);

        Optional<Book> book = bookStore.getBookByISBNStore(isbn);


        if (book.isPresent()) {
            Book foundbook = book.get();
            String titel = foundbook.getTitle();
            System.out.println("The book with the ISBN: " + isbn + ". it's titel is: " + titel);
        } else {
            System.out.println("No book found for ISBN: " + isbn);
        }

        return book;
    }

    public java.util.List<Book> getAllBooks() {
        return bookStore.getAllBooksStore();
    }

    public Optional<Member> getMemberById(int memberId) {
        return memberStore.getMemberByIdStore(memberId);
    }

    public List<Member> getAllMembers() {
        try {
            return memberStore.getAllMembersStore();
        } catch (SQLException e) {
            System.err.println("Error getting all members: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean isSuspendedMember(int memberId) {
        return memberStore.isSuspendedMemberStore(memberId);
    }


    public void permaBanMember(int memberId) throws SQLException, IOException {
        try {
            boolean successban = memberStore.permaBanMemberStore(memberId);
            if (successban) {
                System.out.println("Member with ID: " + memberId + " has been permanently banned.");
            } else {
                // Handle the failure case as needed
                System.out.println("Failed to permanently ban member with ID: " + memberId + ".");
            }
        } catch (SQLException | IOException e) {
            System.err.println("An error occurred while processing the permanent ban for member ID: " + memberId + ": " + e.getMessage());
            logger.error("Failed to permanently ban member with ID: {}: {}", memberId, e.getMessage(), e);
        }
    }

    public void lendBook(int memberId, long isbn) throws SQLException, IOException {


        int successLent = lendingStore.LendBookStore(memberId, isbn);
        switch (successLent) {
            case 1:
                System.out.println("Member with ID: " + memberId + " has successfully lent book with ISBN: " + isbn);
                break;
            case 2:
                System.out.println("Member with ID: " + memberId + " has lent too many books and cannot lend anymore.");
                break;
            case 3:
                System.out.println("Book not available due to no copies");
                break;
            default:
                System.out.println("An error occurred during the lending process.");
                break;
        }
        }



    public void returnItem(int memberId, long isbn) {

        boolean isReturned = lendingStore.returnItemStore(memberId, isbn);

        if (isReturned) {
            // skriver ut meddelandet i manager ist
            System.out.println("Book successfully returned.");
        } else {
            System.out.println("No borrowing record found for this book and member.");
        }
    }

    public boolean autoSuspendMember(int memberId) {
        boolean SuspensionApplied = memberStore.autoSuspendMemberStore(memberId);
        if (SuspensionApplied){
            System.out.println("Member ID: "+ memberId+ " is suspended due to many late books returns");
        }

        return SuspensionApplied;
    }

    public void resetSuspensions() {
        memberStore.resetSuspensionsStore(currentDate);
    }

    public void UpdateBookCopies(long isbn, int NewCopyCount) {
        lendingStore.UpdatebookcopiesStore(isbn, NewCopyCount);
    }

    public boolean checkUserExists(String socialSecurityNumber) throws SQLException {
        return memberStore.checkUserExistsStore(socialSecurityNumber);
    }

    public boolean checkUserBanned (String socialSecurityNumber) throws SQLException {
        return memberStore.checkUserBannedStore(socialSecurityNumber);
    }

    public boolean updateNullMember (Member member) throws SQLException {
        return memberStore.updateNullMemberStore(member);
    }

    public int addNewMember (Member member) throws SQLException {
        return memberStore.addNewMemberStore(member);
    }

}