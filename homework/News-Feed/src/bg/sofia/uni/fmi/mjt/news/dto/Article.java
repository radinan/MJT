package bg.sofia.uni.fmi.mjt.news.dto;

import java.util.Objects;

public class Article {
    private final String title;
    private final String description;
    private final String url;
    private final String publishedAt;
    private final String content;

    public Article(String title, String description,
                   String url, String publishedAt, String content) {

        this.title = title;
        this.description = description;
        this.url = url;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        Article article = (Article) o;
        return Objects.equals(title, article.title) &&
                Objects.equals(description, article.description) &&
                Objects.equals(url, article.url) &&
                Objects.equals(publishedAt, article.publishedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, url, publishedAt);
    }

    public String getContent() {
        return content;
    }
}
