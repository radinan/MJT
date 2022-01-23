package bg.sofia.uni.fmi.mjt.news.dto;

import java.util.List;

public class RequestCriteria {
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

        final String separator = "+";
        StringBuilder concatenatedKeywords = new StringBuilder();

        keywords.forEach(e -> concatenatedKeywords.append(e).append(separator));
        concatenatedKeywords.deleteCharAt(concatenatedKeywords.length() - 1);

        return concatenatedKeywords.toString();
    }

    public String getCategory() {
        return category;
    }

    public String getCountry() {
        return country;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public static RequestBuilder builder(List<String> keywords) {
        return new RequestBuilder(keywords);
    }

    private RequestCriteria(RequestBuilder builder) {
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

        public RequestCriteria build() {
            return new RequestCriteria(this);
        }
    }
}