package facade;

import com.google.gson.Gson;
import entities.Request;
import entities.ResponseError;
import entities.ResponseSuccess;
import exceptions.*;

import java.net.HttpURLConnection;
import java.net.URI;
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

    public ResponseSuccess get(Request request) throws NewsFeedClientException {
        HttpResponse<String> httpResponse = getRequest(request);

        if (httpResponse.statusCode() == HttpURLConnection.HTTP_OK) {
            return GSON.fromJson(httpResponse.body(), ResponseSuccess.class);
        }

        ResponseError responseError = GSON.fromJson(httpResponse.body(), ResponseError.class); //or return everywhere the message?
        final int HTTP_TOO_MANY_REQUESTS = 429;

        if (httpResponse.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
            throw new RequestParameterException("Missing or invalid parameter.");
        }

        if (httpResponse.statusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new ApiKeyException("Missing or invalid api key.");
        }

        if (httpResponse.statusCode() == HTTP_TOO_MANY_REQUESTS) {
            throw new RateLimitException("Too many requests within a window of time.");
        }

        if (httpResponse.statusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new ServerErrorException("Internal error.");
        }

        throw new NewsFeedClientException("Unexpected response code.");
    }

    private HttpResponse<String> getRequest(Request request) throws NewsFeedClientException {
        try {
            URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH, generateFragment(request));
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).build();

            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new NewsFeedClientException("Could not retrieve the articles.", e);
        }
    }

    private String generateFragment(Request request) {
        StringBuilder fragment = new StringBuilder();

        final String PREFIX_COUNTRY = "country=";
        final String PREFIX_CATEGORY = "category=";
        final String PREFIX_KEYWORDS = "q=";
        final String PREFIX_PAGE_SIZE = "pageSize=";
        final String PREFIX_PAGE = "page=";
        final String PREFIX_API_KEY = "apiKey=";

        if (request.getCountry().isPresent()) {
            appendPrefixAndValueToFragment(fragment, PREFIX_COUNTRY, request.getCountry().get());
        }

        if (request.getCategory().isPresent()) {
            appendPrefixAndValueToFragment(fragment, PREFIX_CATEGORY, request.getCategory().get());
        }

        appendPrefixAndValueToFragment(fragment, PREFIX_KEYWORDS, request.getConcatenatedKeywords());

        appendPrefixAndValueToFragment(fragment, PREFIX_PAGE_SIZE, request.getPageSize().toString());

        appendPrefixAndValueToFragment(fragment, PREFIX_PAGE, request.getPage().toString());

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
