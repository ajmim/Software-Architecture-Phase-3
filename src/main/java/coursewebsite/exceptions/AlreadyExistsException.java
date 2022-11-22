package coursewebsite.exceptions;

public class AlreadyExistsException extends Exception {
    private String message;

    public AlreadyExistsException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
