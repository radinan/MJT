package entities;

import dto.Article;
import dto.Status;

import java.util.List;

public class ResponseSuccess {
    private final Status status;
    private final Long totalResults;
    private final List<Article> articles;

    public ResponseSuccess(Status status, Long totalResults, List<Article> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public Status getStatus() {
        return status;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
