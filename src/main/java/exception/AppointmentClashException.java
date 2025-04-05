package exception;

public class AppointmentClashException extends Exception {
    public AppointmentClashException(String message) {
        super(message);
    }
}
