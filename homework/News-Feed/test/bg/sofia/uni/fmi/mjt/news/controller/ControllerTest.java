package bg.sofia.uni.fmi.mjt.news.controller;

import bg.sofia.uni.fmi.mjt.news.dto.Article;
import bg.sofia.uni.fmi.mjt.news.dto.ResponseSuccess;
import bg.sofia.uni.fmi.mjt.news.dto.Request;
import bg.sofia.uni.fmi.mjt.news.dto.Status;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsFeedClientException;
import bg.sofia.uni.fmi.mjt.news.client.NewsHttpClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {
    private static ResponseSuccess responseTotalSizeOne;
    private static ResponseSuccess responseTotalSizeFive;


    @Mock
    private NewsHttpClient newsHttpClientMock;

    @InjectMocks
    private Controller controller;

    private static final Integer MAX_PAGES = 2;
    private static final Integer MAX_PAGE_SIZE = 2;


    @BeforeClass
    public static void setupClass() {
        Article article = new Article("Example title", "Example description",
                "Example url", "Example publishedAt", "Example content");
        List<Article> articles = new ArrayList<>();
        articles.add(article);

        responseTotalSizeOne = new ResponseSuccess(Status.ok, articles.size(), articles);

        for (int i = 1; i < MAX_PAGE_SIZE * MAX_PAGES + 1; ++i) {
            articles.add(article);
        }

        responseTotalSizeFive = new ResponseSuccess(Status.ok, articles.size(), articles);
    }

    @Before
    public void setup() throws NewsFeedClientException {
        controller = new Controller(newsHttpClientMock);
    }

    @Test
    public void testGetNewsOnePageSuccess() throws NewsFeedClientException {
        when(newsHttpClientMock.get(Mockito.any(Request.class))).thenReturn(responseTotalSizeOne);

        List<String> keywords = new ArrayList<>();
        keywords.add("Example");
        List<Article> news = controller.getNews(keywords, Optional.empty(), Optional.empty());

        assertEquals("", news, responseTotalSizeOne.getArticles());
    }

//    @Test
//    public void testGetNewsMultiplePagesSuccess() throws NewsFeedClientException {
//        when(httpRequestSenderMock.get(Mockito.any(Request.class))).thenReturn(responseTotalSizeFive);
//        List<String> keywords = new ArrayList<>();
//        keywords.add("Example");
//        List<Article> news = controller.getNews(keywords, Optional.empty(), Optional.empty());
//
//        assertTrue("", news.size() <= MAX_PAGES*MAX_PAGE_SIZE);
//    }

    @Test(expected = NewsFeedClientException.class)
    public void testExceptionGetNewsMissingKey() throws NewsFeedClientException {
        controller.getNews(null, Optional.empty(), Optional.empty());
    }

    @Test(expected = NewsFeedClientException.class)
    public void testExceptionGetNewsEmptyKey() throws NewsFeedClientException {
        controller.getNews(null, Optional.empty(), Optional.empty());
    }
}
