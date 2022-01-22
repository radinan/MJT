import com.google.gson.Gson;
import dtos.Request;
import dtos.Response;
import exceptions.NewsFeedClientException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpRequestSender {
    private final String API_KEY = "ae844815f7bf46a8b20a25c44a0eeb7f";
    private final String API_ENDPOINT_SCHEME = "https";
    private final String API_ENDPOINT_HOST = "newsapi.org";
    private final String API_ENDPOINT_PATH = "/v2/top-headlines";

    private final Gson GSON = new Gson();

    private final String api_key;
    private final HttpClient httpClient;


    public HttpRequestSender (HttpClient httpClient) {
        this.httpClient = httpClient;
        this.api_key = API_KEY;
    }

    public HttpRequestSender (HttpClient httpClient, String api_key) {
        this.httpClient = httpClient;
        this.api_key = api_key;
    }

    public Response getNews(Request request) throws NewsFeedClientException {
        //validateRequest?
        //-> country i category sa validni
        //-> ima KEYWORDS

        HttpResponse<String> httpResponse;

        try {
            URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH, generateFragment(request));
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).build();

            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new NewsFeedClientException(); //doopravi go
        }

        Response response = GSON.fromJson(httpResponse.body(), Response.class);

        return response;
    }

    private String generateFragment(Request request) {
        StringBuilder fragment = new StringBuilder();

        final String PREFIX_COUNTRY = "country=";
        final String PREFIX_CATEGORY = "category=";
        final String PREFIX_KEYWORDS = "q=";
        final String PREFIX_API_KEY = "apiKey=";

        if (request.country().isPresent()) {
            appendPrefixAndValueToFragment(fragment, PREFIX_COUNTRY, request.country().get());
        }

        if (request.category().isPresent()) {
            appendPrefixAndValueToFragment(fragment, PREFIX_CATEGORY, request.category().get());
        }

        appendPrefixAndValueToFragment(fragment, PREFIX_KEYWORDS, request.concatenatedKeywords());

        appendPrefixAndValueToFragment(fragment, PREFIX_API_KEY, api_key);

        return fragment.toString();
    }

    private void appendPrefixAndValueToFragment(StringBuilder fragment, String prefix, String value) {
        final String AND = "&";

        if (fragment.length() > 0) {
            fragment.append(AND);
        }

        fragment.append(prefix).append(value);
    }
}
