package clane.car.error;

public class CarNotFoundError extends RuntimeException {

    public CarNotFoundError(String Car) {
        super("could not find Car '" + Car + "'.");
    }
}
