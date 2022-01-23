package entities;

import java.util.List;
import java.util.Optional;

public class Request {
    private final List<String> keywords;
    private final String category;
    private final String country;
    private final Integer pageSize;
    private final Integer page;


    public Request(List<String> keywords, String category, String country, Integer pageSize, Integer page) {
        this.keywords = keywords;
        this.category = category;
        this.country = country;
        this.pageSize = pageSize;
        this.page = page;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Optional<String> getCategory() {
        return Optional.of(category);
    }

    public Optional<String> getCountry() {
        return Optional.of(country);
    }

    public String getConcatenatedKeywords() {
        if (keywords.isEmpty()) {
            return null;
        }

        final String SEPARATOR = "+";
        StringBuilder concatenatedKeywords = new StringBuilder();

        keywords.forEach(e -> concatenatedKeywords.append(e).append(SEPARATOR));
        concatenatedKeywords.deleteCharAt(concatenatedKeywords.length() - 1);

        return concatenatedKeywords.toString();
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPage() {
        return page;
    }
}