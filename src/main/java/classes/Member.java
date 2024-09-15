package classes;

import interfaces.ILibraryMember;

public class Member implements ILibraryMember {
    private int id;
    private String firstName;
    private String lastName;
    private int memberType;

    private int maxborrow;

    private String socialSecNr;

    // Constructor
    public Member(int id, String firstName, String lastName, int memberType, String socialSecNr) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.memberType = memberType;
        this.socialSecNr = socialSecNr;
    }

    // ID Getter
    @Override
    public int getId() {
        return id;
    }

    // ID Setter
    public void setId(int id) {
        this.id = id;
    }

    // First Name Getter
    @Override
    public String getFirstName() {
        return firstName;
    }

    // First Name Setter
    public void setFirstName(String name) {
        this.firstName = firstName;
    }

    // Last Name Getter
    @Override
    public String getLastName() {
        return lastName;
    }

    // Last Name Setter
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Member Type Getter
    @Override
    public int getMemberType() {
        return memberType;
    }

    @Override
    public String getSocialSecNr() {
        return socialSecNr;
    }

    // Member Type Setter
    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }


    public int getMaxBorrowLimit(){
        this.maxborrow = maxborrow;
        return maxborrow;
    }

    // ToString Method
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", memberType=" + memberType +
                '}';
    }
}
