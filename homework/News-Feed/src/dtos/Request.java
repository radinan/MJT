package dtos;

import java.util.List;
import java.util.Optional;

public record Request(List<String> keywords,
                      Optional<String> category,
                      Optional<String> country) {

    public String concatenatedKeywords() {
        if (keywords.isEmpty()) {
            return null;
        }

        StringBuilder concatenatedKeywords = new StringBuilder();
        final String SEPARATOR = "+";

        keywords.forEach(e -> concatenatedKeywords.append(e).append(SEPARATOR));
        concatenatedKeywords.deleteCharAt(concatenatedKeywords.length() - 1);

        return concatenatedKeywords.toString();
    }
}