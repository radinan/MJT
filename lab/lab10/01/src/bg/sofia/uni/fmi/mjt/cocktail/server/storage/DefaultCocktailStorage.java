package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DefaultCocktailStorage implements CocktailStorage {
    private final Collection<Cocktail> cocktails;

    public DefaultCocktailStorage() {
        this.cocktails = new ArrayList<>();
    }

    @Override
    public void createCocktail(Cocktail cocktail) throws CocktailAlreadyExistsException {
        if (cocktails.stream().anyMatch(e -> e.name().equals(cocktail.name()))) {
            throw new CocktailAlreadyExistsException(String.format("cocktail %s already exists", cocktail.name()));
        }

        cocktails.add(cocktail);
    }

    @Override
    public Collection<Cocktail> getCocktails() {
        return Collections.unmodifiableCollection(cocktails);
    }

    @Override
    public Collection<Cocktail> getCocktailsWithIngredient(String ingredientName) {
        return cocktails.stream()
                .filter(e -> e.ingredients().stream().anyMatch(i -> i.name().equals(ingredientName)))
                .toList();
    }

    @Override
    public Cocktail getCocktail(String name) throws CocktailNotFoundException {
        List<Cocktail> cocktail = cocktails.stream().filter(e -> e.name().equals(name)).toList();

        if (cocktail.isEmpty()) {
            throw new CocktailNotFoundException(String.format("cocktail with name %s not found", name));
        }

        return cocktail.get(0);
    }
}
