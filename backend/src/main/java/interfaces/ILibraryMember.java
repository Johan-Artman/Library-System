package interfaces;

public interface ILibraryMember {
    /**
     * Retrieves the unique ID of the library member.
     *
     * @return Member's ID as an integer.
     */
    int getId();

    /**
     * Retrieves the first name of the library member.
     *
     * @return Member's first name as a String.
     */
    String getFirstName();

    /**
     * Retrieves the last name of the library member.
     *
     * @return Member's last name as a String.
     */
    String getLastName();

    /**
     * Retrieves the type of the library member (e.g., Undergraduate, Postgraduate).
     *
     * @return MemberType enumeration value.
     */
    int getMemberType();

    /**
     * Retrieves the maximum number of items the member is allowed to borrow.
     *
     * @return Maximum borrow limit as an integer.
     */

    String getSocialSecNr();
    /**
     * Retrieves the social security number of the person.
     *
     * @return Member's social security number as a String.
     */
}
