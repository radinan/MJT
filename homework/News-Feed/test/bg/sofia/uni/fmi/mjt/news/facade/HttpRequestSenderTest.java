package bg.sofia.uni.fmi.mjt.news.facade;

import bg.sofia.uni.fmi.mjt.news.dto.Article;
import bg.sofia.uni.fmi.mjt.news.dto.ResponseSuccess;
import bg.sofia.uni.fmi.mjt.news.entities.Request;
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
public class HttpRequestSenderTest {
    private static Request request;
    private static String exampleJson;
    private static ResponseSuccess exampleResponse;

    @Mock
    private static HttpResponse<String> httpResponse;

    @Mock
    private HttpClient httpClientMock;

    @InjectMocks
    private HttpRequestSender httpRequestSender;

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
                .thenReturn(httpResponse);

        httpRequestSender = new HttpRequestSender(httpClientMock);
    }

    @Test
    public void getSuccess() throws NewsFeedClientException {
        when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponse.body()).thenReturn(exampleJson);

        var response = httpRequestSender.get(request);

        assertEquals("", response, exampleResponse);
    }
}
