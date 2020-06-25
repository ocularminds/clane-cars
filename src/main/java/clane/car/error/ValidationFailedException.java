package clane.car.error;

public class ValidationFailedException extends RuntimeException {

    public ValidationFailedException(String msg) {
        super(msg);
    }
}
