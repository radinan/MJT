package bg.sofia.uni.fmi.mjt.news.controller;

import bg.sofia.uni.fmi.mjt.news.dto.Article;
import bg.sofia.uni.fmi.mjt.news.dto.ResponseSuccess;
import bg.sofia.uni.fmi.mjt.news.entities.Status;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsFeedClientException;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    private static List<Article> exampleArticles;
    private static ResponseSuccess exampleResponse;
    private static String exampleResponseJSON;

    @Mock
    private HttpClient newsFeedHttpClientMock;

    @Mock
    private HttpResponse<String> newsFeedHttpResponseMock;

    @InjectMocks
    private static Controller controller;


    @BeforeClass
    public static void setupClass() {
        Article article = new Article(
                "Example title",
                "Example description",
                "Example url",
                "Example published at",
                "Example content"
                );

        exampleArticles = new ArrayList<>();
        exampleArticles.add(article);

        exampleResponse = new ResponseSuccess(Status.ok, 1, exampleArticles);
        exampleResponseJSON = new Gson().toJson(exampleResponse);
    }

    @Before
    public void setup() throws IOException, InterruptedException {
        when(newsFeedHttpClientMock.send(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(newsFeedHttpResponseMock);

        controller = new Controller(newsFeedHttpClientMock);
    }

    @Test
    public void testGetNewsValid() throws NewsFeedClientException {
        when(newsFeedHttpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(newsFeedHttpResponseMock.body()).thenReturn(exampleResponseJSON);

        List<String> keywords = new ArrayList<>();
        keywords.add("Example");

        List<Article> articles = controller.getNewsFeed(keywords, java.util.Optional.empty(), java.util.Optional.empty());

        assertEquals("Incorrect news for valid parameters.", exampleArticles, articles);
    }
}
