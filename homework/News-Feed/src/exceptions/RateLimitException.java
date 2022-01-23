package exceptions;

public class RateLimitException extends NewsFeedClientException {
    public RateLimitException(String message) {
        super(message);
    }
}
