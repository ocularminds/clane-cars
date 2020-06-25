package clane.car.error;

public class JsonParserError extends RuntimeException {

    public JsonParserError(final String json) {
        super("Unable to parser input json: '" + json + "'.");
    }
}
