package entities;

import java.util.List;
import java.util.Optional;

public class Request {
    private final List<String> keywords;

    private final String category;
    private final String country;
    private final Integer pageSize;
    private final Integer page;

    public List<String> getKeywords() {
        return keywords;
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

    public Optional<String> getCategory() {
        return Optional.of(category);
    }

    public Optional<String> getCountry() {
        return Optional.of(country);
    }

    public Optional<Integer> getPageSize() {
        return pageSize.describeConstable();
    }

    public Optional<Integer> getPage() {
        return page.describeConstable();
    }

    public static RequestBuilder builder(List<String> keywords) {
        return new RequestBuilder(keywords);
    }

    private Request(RequestBuilder builder) {
        this.keywords = builder.keywords;
        this.category = builder.category;
        this.country = builder.country;
        this.pageSize = builder.pageSize;
        this.page = builder.page;
    }

    public static class RequestBuilder {
        private List<String> keywords;

        private String category;
        private String country;
        private Integer pageSize;
        private Integer page;

        private RequestBuilder(List<String> keywords) {
            this.keywords = keywords;
        }

        public RequestBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public RequestBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public RequestBuilder setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public RequestBuilder setPage(Integer page) {
            this.page = page;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}