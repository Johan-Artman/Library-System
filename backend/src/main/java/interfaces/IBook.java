package interfaces;

public interface IBook {
    /**
     * Retrieves the ISBN of the book.
     *
     * @return Book's ISBN as an integer.
     */
    long getISBN();

    /**
     * Retrieves the title of the book.
     *
     * @return Book's title as a String.
     */
    String getTitle();

    /**
     * Retrieves the number of available copies of the book.
     *
     * @return Number of available copies as an integer.
     */
    int getAvailableCopies();
}
