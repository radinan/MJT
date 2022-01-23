package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.dto.RequestCriteria;
import bg.sofia.uni.fmi.mjt.news.dto.Response;
import bg.sofia.uni.fmi.mjt.news.dto.Article;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsFeedException;
import bg.sofia.uni.fmi.mjt.news.client.NewsHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewsFeed {

    private static final Integer MAX_PAGES = 2;
    private static final Integer MAX_PAGE_SIZE = 10;

    private final Integer maxPages;
    private final Integer maxPageSize;
    private Integer currentPage;

    private final NewsHttpClient newsHttpClient;

    public NewsFeed(NewsHttpClient newsHttpClient) {
        currentPage = 1;
        maxPages = MAX_PAGES;
        maxPageSize = MAX_PAGE_SIZE;
        this.newsHttpClient = newsHttpClient;
    }

    public NewsFeed(NewsHttpClient newsHttpClient, Integer maxPages, Integer maxPageSize) {
        currentPage = 1;
        this.maxPages = maxPages;
        this.maxPageSize = maxPageSize;
        this.newsHttpClient = newsHttpClient;
    }

    public List<Article> getNews(List<String> keywords, Optional<String> category, Optional<String> country)
            throws NewsFeedException {

        if (keywords == null || keywords.isEmpty()) {
            throw new NewsFeedException("Missing required keywords parameter.");
        }

        RequestCriteria.RequestBuilder requestBuilder = RequestCriteria.builder(keywords);
        category.ifPresent(requestBuilder::setCategory);
        country.ifPresent(requestBuilder::setCountry);
        requestBuilder.setPageSize(maxPageSize);
        requestBuilder.setPage(currentPage);

        Response response = newsHttpClient.get(requestBuilder.build());

        List<Article> allNews = new ArrayList<>(response.getArticles());

        while (currentPage < maxPages && response.getTotalResults() > (long) maxPageSize * currentPage) {
            response = newsHttpClient.get(requestBuilder.setPageSize(++currentPage).build());
            allNews.addAll(response.getArticles());
        }

        return allNews;
    }

}
