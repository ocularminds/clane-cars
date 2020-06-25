package clane.car.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParamsError extends RuntimeException {

    private static final long serialVersionUID = -3915153143490977128L;

    public InvalidParamsError(String msg) {
        super(msg);
    }
}
