package bg.sofia.uni.fmi.mjt.news.client;

import bg.sofia.uni.fmi.mjt.news.dto.Article;
import bg.sofia.uni.fmi.mjt.news.dto.ResponseSuccess;
import bg.sofia.uni.fmi.mjt.news.dto.Request;
import bg.sofia.uni.fmi.mjt.news.dto.Status;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsFeedClientException;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsServiceException;
import bg.sofia.uni.fmi.mjt.news.exceptions.RequestParameterException;
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
    private static Request request;
    private static String exampleJson;
    private static ResponseSuccess exampleResponse;

    @Mock
    private static HttpResponse<String> httpResponseMock;

    @Mock
    private HttpClient httpClientMock;

    @InjectMocks
    private NewsHttpClient newsHttpClient;

    @BeforeClass
    public static void setupClass() {
        List<String> keywords = new ArrayList<>();
        keywords.add("Example");
        request = Request.builder(keywords).build();

        Article article = new Article("Example title", "Example description",
                "Example url", "Example publishedAt", "Example content");
        List<Article> articles = new ArrayList<>();
        articles.add(article);

        exampleResponse = new ResponseSuccess(Status.ok, 1, articles);
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
    public void testGetSuccess() throws NewsFeedClientException {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(exampleJson);

        var response = newsHttpClient.get(request);

        assertEquals("", response, exampleResponse);
    }

    @Test
    public void testGetNewsServiceException() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);
        try {
            newsHttpClient.get(request);
        } catch (Exception e) {
            assertEquals("", NewsServiceException.class, e.getClass());
        }

        final int HTTP_TOO_MANY_REQUESTS = 429;
        when(httpResponseMock.statusCode()).thenReturn(HTTP_TOO_MANY_REQUESTS);
        try {
            newsHttpClient.get(request);
        } catch (Exception e) {
            assertEquals("", NewsServiceException.class, e.getClass());
        }
    }

    @Test
    public void testGetRequestParameterException() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        try {
            newsHttpClient.get(request);
        } catch (Exception e) {
            assertEquals("", RequestParameterException.class, e.getClass());
        }

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
        try {
            newsHttpClient.get(request);
        } catch (Exception e) {
            assertEquals("", RequestParameterException.class, e.getClass());
        }
    }

    @Test
    public void testGetUnexpectedStatusCode() {
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_IMPLEMENTED);
        try {
            newsHttpClient.get(request);
        } catch (Exception e) {
            assertEquals("", NewsFeedClientException.class, e.getClass());
            assertNotEquals("", NewsServiceException.class, e.getClass());
            assertNotEquals("", RequestParameterException.class, e.getClass());
        }
    }

    @Test
    public void testGetRethrownException() throws IOException, InterruptedException {
        IOException expectedExc = new IOException();
        when(httpClientMock.send(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenThrow(expectedExc);

        try {
            newsHttpClient.get(request);
        } catch (NewsFeedClientException e) {
            assertEquals("", e.getCause(), expectedExc);
//            assertEquals(); type
        }
    }
}
