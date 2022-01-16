package bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions;

public class CocktailNotFoundException extends Throwable {
    private final String message;

    public CocktailNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
