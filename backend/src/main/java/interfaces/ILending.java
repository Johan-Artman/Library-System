package interfaces;

public interface ILending {
    /**
     * Lends a book an item for a library member.
     *
     * @param memberId The ID of the member borrowing the item.
     * @param isbn     The ISBN of the book being borrowed.
     */
    void LendBook(int memberId, long isbn);

    /**
     * Returns an item borrowed by a library member.
     *
     * @param memberId The ID of the member returning the item.
     * @param isbn     The ISBN of the book being returned.
     */
    void returnItem(int memberId, long isbn);
    // Additional methods as required

    void Updatebookcopies (long isbn, int newCopyCount);

}

