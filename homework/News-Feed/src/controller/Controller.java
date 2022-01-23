package controller;

import entities.Request;
import entities.ResponseSuccess;
import dto.Article;
import exceptions.NewsFeedClientException;
import facade.HttpRequestSender;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//singleton?
public class Controller {
    private final Integer MAX_PAGES = 2;
    private final Integer MAX_PAGE_SIZE = 10;
    private Integer currentPage;

    private final HttpRequestSender httpRequestSender;


    public Controller(HttpClient httpClient) {
        currentPage = 1;
        httpRequestSender = new HttpRequestSender(httpClient);
    }

    public Controller(HttpClient httpClient, String apiKey) {
        currentPage = 1;
        httpRequestSender = new HttpRequestSender(httpClient, apiKey);
    }

    //make category and country optional?
    //rethrow with better messages?
    public List<Article> getNewsFeed(List<String> keywords, Optional<String> category, Optional<String> country) throws NewsFeedClientException {
        if (keywords == null || keywords.isEmpty()) {
            throw new NewsFeedClientException("Missing required parameter.");
        }

        Request.RequestBuilder requestBuilder = Request.builder(keywords);
        category.ifPresent(requestBuilder::setCategory);
        country.ifPresent(requestBuilder::setCountry);
        requestBuilder.setPageSize(MAX_PAGE_SIZE);
        requestBuilder.setPage(currentPage);

        ResponseSuccess response = httpRequestSender.get(requestBuilder.build());

        List<Article> allNews = new ArrayList<>(response.getArticles());

        while (currentPage < MAX_PAGES && response.getTotalResults() > (long) MAX_PAGE_SIZE * currentPage) {
            response = httpRequestSender.get(requestBuilder.setPageSize(++currentPage).build());
            allNews.addAll(response.getArticles());
        }

        return allNews;
    }
}
