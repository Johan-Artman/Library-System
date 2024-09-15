package exceptions;

public class MemberExistsException extends Exception {
    public MemberExistsException(String message) {
        super(message);
    }
}