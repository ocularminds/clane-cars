package clane.car.error;

public class NotFoundError extends RuntimeException {

    public NotFoundError(String name) {
        super("could not find '" + name + "'.");
    }
}
