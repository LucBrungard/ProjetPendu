package exceptions;

public class RequestHandlerException extends RuntimeException {
    public RequestHandlerException(String message) {
        super(message);
    }
}
