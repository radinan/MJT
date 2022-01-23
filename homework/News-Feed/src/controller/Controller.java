package controller;

import entities.Request;
import entities.ResponseSuccess;
import dto.Article;
import exceptions.NewsFeedClientException;
import facade.HttpRequestSender;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

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
    public List<Article> getNewsFeed(List<String> keywords, String category, String country) throws NewsFeedClientException {
        if (keywords == null || keywords.isEmpty()) {
            throw new NewsFeedClientException("Missing required parameter.");
        }

        ResponseSuccess response = httpRequestSender.get(
                new Request(keywords, category, country, MAX_PAGE_SIZE, currentPage));

        List<Article> allNews = new ArrayList<>(response.getArticles());

        while (currentPage < MAX_PAGES && response.getTotalResults() > (long) MAX_PAGE_SIZE * currentPage) {
            response = httpRequestSender.get(new Request(keywords, category, country, MAX_PAGE_SIZE, ++currentPage));
            allNews.addAll(response.getArticles());
        }

        return allNews;
    }
}
