package textinterface;

import classes.LibraryStoreManager;
import classes.CurrentDate;
import classes.Member;
import exceptions.MemberBannedException;
import exceptions.MemberExistsException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class textinterface_Main {
    private static final Scanner scan = new Scanner(System.in);
    public static CurrentDate currentDate = new CurrentDate();
    private static LibraryStoreManager libraryStoreManager = new LibraryStoreManager(currentDate);


    public static void main(String[] args) throws MemberBannedException, MemberExistsException, SQLException, IOException {
        updateCurrentDate();


        System.out.println("Select your role:\n1 - Librarian\n2 - Student");
        int role = scan.nextInt();
        scan.nextLine();

        if (role == 1) {
            librarianInterface();
        } else if (role == 2) {
            studentInterface();
        } else {
            System.out.println("Invalid role selected.");
        }
    }

    private static void librarianInterface() throws MemberBannedException, MemberExistsException, SQLException, IOException {

        boolean loop = true;
        while (loop) {
            System.out.println("Welcome librarian\n choose an option:");
            System.out.println("1 - Manage Members (Register/Remove/Ban)");
            System.out.println("2 - Manage Books (Search/Lend/Return)");
            System.out.println("3 - Update Current Date");
            System.out.println("4 - Exit");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1: // Manage Members
                    manageMembers();
                    break;
                case 2: // Manage Books
                    manageBooks();
                    break;
                case 3: // Change current date
                    updateCurrentDate();
                    break;
                case 4: // Exit
                    loop = false;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
    }

    private static void studentInterface() throws SQLException, IOException, MemberBannedException, MemberExistsException {
        manageBooks();
    }


    public static void manageMembers() throws MemberBannedException, MemberExistsException, SQLException, IOException {
        boolean loop = true;
        while (loop) {
            System.out.println("1 - Register Member");
            System.out.println("2 - Remove Member");
            System.out.println("3 - Ban Member");
            System.out.println("4 - Return to previous menu");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1: // add member
                    LocalDate birthDate = null;
                    boolean validSsnDate = false;


                    System.out.println("enter member's first name:");
                    String firstName = scan.nextLine();
                    System.out.println("enter member's last name:");
                    String lastName = scan.nextLine();
                    System.out.println("enter member's social security number:");
                    String socSecNum = scan.nextLine();

                    while (!validSsnDate) {
                        try {
                            String datePart = socSecNum.substring(0, 8);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                            birthDate = LocalDate.parse(datePart, formatter);

                            if (birthDate.isAfter(LocalDate.now())) {
                                // SSN nummer i framtiden, försök igen
                                System.err.println("The social security number date cannot be in the future. Please enter a valid SSN.");
                                socSecNum = scan.nextLine();
                            } else {
                                validSsnDate = true; // SSN nummer giltigt.
                            }
                        } catch (DateTimeParseException | StringIndexOutOfBoundsException e) {

                            System.err.println("Invalid social security number format. Please use the format YYYYMMDDXXXX.");
                            socSecNum = scan.nextLine();
                        }
                    }
                    System.out.println("enter member's type\n" +
                            "1: Undergraduate\n" +
                            "2: Postgraduate\n" +
                            "3: PHD\n" +
                            "4: TEACHER");
                    int memberType = scan.nextInt();
                    scan.nextLine(); // Consume newline
                    Member newMember = new Member(0, firstName, lastName, memberType, socSecNum);

                    libraryStoreManager.addMember(newMember);
                    break;
                case 2: // Remove member
                    System.out.println("Enter member id to remove member");
                    int memberId = scan.nextInt();
                    libraryStoreManager.removeMember(memberId);
                    break;
                case 3: // Ban member
                    System.out.println("Enter the member id to permanently ban a member");
                    int idBan = scan.nextInt();
                    libraryStoreManager.permaBanMember(idBan);
                    break;
                case 4: // Exit
                    loop = false;
                    librarianInterface();
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
    }

    private static void manageBooks() throws SQLException, IOException, MemberBannedException, MemberExistsException {
        boolean booksLoop = true;
        while (booksLoop) {
            System.out.println("Choose an option:");
            System.out.println("1 - Search book by ISBN");
            System.out.println("2 - Lend a book");
            System.out.println("3 - Return a book");
            System.out.println("4 - Return to previous menu");

            int booksChoice = scan.nextInt();
            scan.nextLine(); // Handle the newline character

            switch (booksChoice) {
                case 1: // Sök bok ISBN

                    System.out.println("Enter ISBN of book");

                    long searchISBN = scan.nextLong();
                    libraryStoreManager.getBookByISBN(searchISBN);
                    break;

                case 2: // LÅNA BOK
                    System.out.println("Enter member ID");
                    int memberId = scan.nextInt();
                    if (!libraryStoreManager.isSuspendedMember(memberId)) {
                        if (libraryStoreManager.autoSuspendMember(memberId)) {
                            break;
                        } else {
                            System.out.println("Enter book ISBN");
                            long lendISBN = scan.nextLong();
                            libraryStoreManager.lendBook(memberId, lendISBN);
                        }
                        break;
                    } else {
                        System.out.println("member is banned");}
                        case 3: // RETUNERA BOK
                            System.out.println("Enter member ID");
                            int returnMemberId = scan.nextInt();


                            System.out.println("Enter book ISBN");
                            long returnISBN = scan.nextLong();
                            libraryStoreManager.returnItem(returnMemberId, returnISBN);
                            libraryStoreManager.autoSuspendMember(returnMemberId);
                            break;
                        case 4: // Tillbaka till main-menu.
                            booksLoop = false;
                            librarianInterface();
                            break;
                        default:
                            System.out.println("Invalid input. Please try again.");
                            break;
                    }
            }
        }


        public static void updateCurrentDate () {
            boolean validDate = false;
            while (!validDate) {
                System.out.println("Enter a date yyyy-mm-dd:");
                String inputDate = scan.nextLine();
                try {
                    LocalDate userDefinedDate = LocalDate.parse(inputDate);

                    LocalDate datumidag = LocalDate.now();
                    if (userDefinedDate.isAfter(datumidag)) {
                        System.out.println("The date cannot be in the future Pleas enter a valid date");
                    } else {
                        textinterface_Main.currentDate.setCurrentDate(userDefinedDate);
                        libraryStoreManager.resetSuspensions();
                        validDate = true;
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format. Please use YYYY-MM-DD.");
                }
            }
        }
    }
