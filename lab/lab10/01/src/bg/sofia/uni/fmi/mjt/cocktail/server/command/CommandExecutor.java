package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.CocktailStorage;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.Collection;
import java.util.HashSet;

import com.google.gson.Gson;

public class CommandExecutor {

    private final String create = "create";
    private final String get = "get";
    private final String disconnect = "disconnect";

    private CocktailStorage storage;

    private final Gson gson = new Gson();

    public CommandExecutor(CocktailStorage storage) {
        this.storage = storage;
    }

    public String execute(Command cmd) {
        if (cmd == null) {
            return null;
        }

        return switch (cmd.command()) {
            case create -> create(cmd.arguments());
            case get -> get(cmd.arguments());
            case disconnect -> "disconnect";
            default -> null;
        };
    }

    private String create(String arguments) {
        if (arguments == null) {
            return null;
        }

        String[] parsedArguments = arguments.split(" ", 2);

        String name = parsedArguments[0];
        String[] pairs = parsedArguments[1].split(" ");

        Cocktail cocktail = new Cocktail(name, new HashSet<>());
        for (String p : pairs) {
            String[] pair = p.split("=");
            cocktail.ingredients().add(new Ingredient(pair[0], pair[1]));
        }

        try {
            storage.createCocktail(cocktail);
            return gson.toJson(new Response("CREATED", null, null, null));
        } catch (CocktailAlreadyExistsException e) {
            return gson.toJson(new Response("ERROR", e.getMessage(), null, null));
        }
    }

    private String get(String arguments) {
        if (arguments == null) {
            return null;
        }

        String[] parsedArguments = arguments.split(" ");

        String type = parsedArguments[0];

        switch (type) {
            case "all" -> {
                Collection<Cocktail> cocktails = storage.getCocktails();
                return gson.toJson(new Response("OK", null, cocktails, null));
            }
            case "by-name" -> {
                try {
                    Cocktail cocktail = storage.getCocktail(parsedArguments[1]);
                    return gson.toJson(new Response("OK", null, null, cocktail));
                } catch (CocktailNotFoundException e) {
                    return gson.toJson(new Response("ERROR", e.getMessage(), null, null));
                }
            }
            case "by-ingredient" -> {
                Collection<Cocktail> cocktails = storage.getCocktailsWithIngredient(parsedArguments[1]);
                return gson.toJson(new Response("OK", null, cocktails, null));
            }
            default -> {
                return null;
            }
        }
    }
}
