package bg.sofia.uni.fmi.mjt.news.dto;

import java.util.List;
import java.util.Objects;

public class ResponseSuccess {
    private final Status status;
    private final Integer totalResults;
    private final List<Article> articles;

    public ResponseSuccess(Status status, Integer totalResults, List<Article> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseSuccess)) return false;
        ResponseSuccess that = (ResponseSuccess) o;
        return status == that.status && Objects.equals(totalResults, that.totalResults) && Objects.equals(articles, that.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, totalResults, articles);
    }
}
