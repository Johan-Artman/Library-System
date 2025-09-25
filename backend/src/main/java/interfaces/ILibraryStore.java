package interfaces;

public interface ILibraryStore {
    /**
     * Adds a new member to the library.
     *
     * @param member The LibraryMember object to add.
     */
    void addMember(ILibraryMember member);

    /**
     * Removes a member from the library.
     *
     * @param memberId The ID of the member to remove.
     */
    void removeMember(int memberId);

    /**
     * Adds a new book to the library.
     *
     * @param book The Book object to add.
     */
    void addBook(IBook book);

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn The ISBN of the book to retrieve.
     */
        void getBookByISBN(long isbn); //bytt till long ist för int då isbn numret är för långt

    /**
     * Retrieves a library member by their ID.
     *
     * @param memberId The ID of the member to retrieve.
     */
    void getMemberById(int memberId);
    // Additional methods as required
 boolean isSuspendedMember(int memberid);
   //should be a bool value
    public void suspendMember(int memberid);

}
