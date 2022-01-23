package bg.sofia.uni.fmi.mjt.news.client;

import bg.sofia.uni.fmi.mjt.news.dto.Article;
import bg.sofia.uni.fmi.mjt.news.dto.Response;
import bg.sofia.uni.fmi.mjt.news.dto.RequestCriteria;
import bg.sofia.uni.fmi.mjt.news.dto.Status;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsFeedException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ClientServiceException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ClientRequestException;
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
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NewsHttpClientTest {
    private static RequestCriteria requestCriteria;
    private static Response exampleResponse;
    private static String exampleJson;

    @Mock
    private static HttpResponse<String> httpResponseMock;

    @Mock
    private HttpClient httpClientMock;

    @InjectMocks
    private NewsHttpClient newsHttpClient;

    @BeforeClass
    public static void setupClass() {
        List<String> keywords = new ArrayList<>();
        keywords.add("example");

        String category = "business";
        String country = "bg";
        Integer pageSize = 2;
        Integer page = 1;

        requestCriteria = RequestCriteria.builder(keywords)
                .setCategory(category)
                .setCountry(country)
                .setPageSize(pageSize)
                .setPage(page)
                .build();


        String title = "example title";
        String description = "example description";
        String url = "example url";
        String publishedAt = "example publishedAt";
        String content = "example content";

        Article article = new Article(title, description, url, publishedAt, content);
        List<Article> articles = new ArrayList<>();
        articles.add(article);

        exampleResponse = new Response(Status.ok, 1, articles);
        exampleJson = new Gson().toJson(exampleResponse);
    }

    @Before
    public void setup() throws IOException, InterruptedException {
        when(httpClientMock.send(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        newsHttpClient = new NewsHttpClient(httpClientMock);
    }

    @Test
    public void testGetSuccess() throws NewsFeedException {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(exampleJson);

        var response = newsHttpClient.get(requestCriteria);

        assertEquals("Invalid response for valid request criteria.", response, exampleResponse);
    }

    @Test
    public void testGetNewsServiceException() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);
        try {
            newsHttpClient.get(requestCriteria);
        } catch (Exception e) {
            assertEquals("Error class should be ClientServiceException due to server unavailability.",
                    ClientServiceException.class, e.getClass());
        }

        final int httpTooManyRequests = 429;
        when(httpResponseMock.statusCode()).thenReturn(httpTooManyRequests);
        try {
            newsHttpClient.get(requestCriteria);
        } catch (Exception e) {
            assertEquals("Error class should be ClientServiceException due to big amount of requests.",
                    ClientServiceException.class, e.getClass());
        }
    }

    @Test
    public void testGetRequestParameterException() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        try {
            newsHttpClient.get(requestCriteria);
        } catch (Exception e) {
            assertEquals("Error class should be ClientRequestException due to unacceptable request.",
                    ClientRequestException.class, e.getClass());
        }

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
        try {
            newsHttpClient.get(requestCriteria);
        } catch (Exception e) {
            assertEquals("Error class should be ClientRequestException due to unauthorized request.",
                    ClientRequestException.class, e.getClass());
        }
    }

    @Test
    public void testGetUnexpectedStatusCode() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_IMPLEMENTED);
        try {
            newsHttpClient.get(requestCriteria);
        } catch (Exception e) {
            assertEquals("Exception type should be NewsFeedException due to unknown status code.",
                    NewsFeedException.class, e.getClass());
            assertNotEquals("Improper use of ClientServiceException.",
                    ClientServiceException.class, e.getClass());
            assertNotEquals("Improper use of ClientRequestException.",
                    ClientRequestException.class, e.getClass());
        }
    }

    @Test
    public void testGetRethrownException() throws IOException, InterruptedException {
        IOException expectedExc = new IOException();
        when(httpClientMock.send(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenThrow(expectedExc);

        try {
            newsHttpClient.get(requestCriteria);
        } catch (NewsFeedException e) {
            assertEquals("Exception should be wrapped properly by NewsFeedException.",
                    e.getCause(), expectedExc);
        }
    }
}
