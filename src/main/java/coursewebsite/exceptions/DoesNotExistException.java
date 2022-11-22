package coursewebsite.exceptions;

public class DoesNotExistException extends Exception {
    private String message;

    public DoesNotExistException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
