package exceptions;

public class ServerErrorException extends NewsFeedClientException {
    public ServerErrorException(String message) {
        super(message);
    }
}
