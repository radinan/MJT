package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;

import java.util.Collection;

public record Response(String status, String errorMessage, Collection<Cocktail> cocktails, Cocktail cocktail) {
}
