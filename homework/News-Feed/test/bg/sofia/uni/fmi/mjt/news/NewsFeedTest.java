package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.dto.Article;
import bg.sofia.uni.fmi.mjt.news.dto.Response;
import bg.sofia.uni.fmi.mjt.news.dto.RequestCriteria;
import bg.sofia.uni.fmi.mjt.news.dto.Status;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsFeedException;
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
public class NewsFeedTest {
    private static Response responseTotalSizeBelowMax;
    private static Response responseTotalSizeAboveMax;

    @Mock
    private NewsHttpClient newsHttpClientMock;

    @InjectMocks
    private NewsFeed newsFeed;

    private static final Integer MAX_PAGES = 2;
    private static final Integer MAX_PAGE_SIZE = 2;

    @BeforeClass
    public static void setupClass() {
        Article article = new Article("Example title", "Example description",
                "Example url", "Example publishedAt", "Example content");
        List<Article> articles = new ArrayList<>();

        for (int i = 0; i < MAX_PAGE_SIZE; ++i) {
            articles.add(article);
        }

        responseTotalSizeBelowMax = new Response(Status.ok, MAX_PAGE_SIZE, articles);
        responseTotalSizeAboveMax = new Response(Status.ok, MAX_PAGE_SIZE * MAX_PAGES + 1, articles);
    }

    @Before
    public void setup() throws NewsFeedException {
        newsFeed = new NewsFeed(newsHttpClientMock);
    }

    @Test
    public void testGetNewsOnePageSuccess() throws NewsFeedException {
        when(newsHttpClientMock.get(Mockito.any(RequestCriteria.class))).thenReturn(responseTotalSizeBelowMax);

        List<String> keywords = new ArrayList<>();
        keywords.add("Example");
        List<Article> news = newsFeed.getNews(keywords, Optional.empty(), Optional.empty());

        assertEquals("Incorrect news for valid parameters.", news, responseTotalSizeBelowMax.getArticles());
    }

    @Test
    public void testGetNewsMultiplePagesSuccess() throws NewsFeedException {
        when(newsHttpClientMock.get(Mockito.any(RequestCriteria.class))).thenReturn(responseTotalSizeAboveMax);

        List<String> keywords = new ArrayList<>();
        keywords.add("Example");
        List<Article> news = newsFeed.getNews(keywords, Optional.empty(), Optional.empty());

        assertTrue("Incorrect size of returned articles.", news.size() <= MAX_PAGES * MAX_PAGE_SIZE);
    }

    @Test(expected = NewsFeedException.class)
    public void testExceptionGetNewsMissingKey() throws NewsFeedException {
        newsFeed.getNews(null, Optional.empty(), Optional.empty());
    }

    @Test(expected = NewsFeedException.class)
    public void testExceptionGetNewsEmptyKey() throws NewsFeedException {
        newsFeed.getNews(new ArrayList<>(), Optional.empty(), Optional.empty());
    }
}
