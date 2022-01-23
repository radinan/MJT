package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.client.NewsHttpClient;
import bg.sofia.uni.fmi.mjt.news.dto.Article;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsFeedException;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        HttpClient client =
                HttpClient.newBuilder().executor(executor).build();

        NewsHttpClient newsHttpClient = new NewsHttpClient(client, "ae844815f7bf46a8b20a25c44a0eeb7f");
        NewsFeed newsFeed = new NewsFeed(newsHttpClient);

        List<String> keywords = new ArrayList<>();
        keywords.add("trump");
        List<Article> articles = new ArrayList<>();

        try {
            articles = newsFeed.getNews(keywords, Optional.empty(), Optional.empty());
            System.out.println();
        } catch (NewsFeedException e) {
            e.printStackTrace();
        }

        System.out.println();
    }
}
