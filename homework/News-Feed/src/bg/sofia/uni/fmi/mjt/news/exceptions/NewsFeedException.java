package bg.sofia.uni.fmi.mjt.news.exceptions;

public class NewsFeedException extends Exception {

    public NewsFeedException(String message) {
        super(message);
    }

    public NewsFeedException(String message, Exception e) {
        super(message, e);
    }

}
