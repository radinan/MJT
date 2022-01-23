package bg.sofia.uni.fmi.mjt.news.client;

import com.google.gson.Gson;
import bg.sofia.uni.fmi.mjt.news.dto.RequestCriteria;
import bg.sofia.uni.fmi.mjt.news.dto.Response;
import bg.sofia.uni.fmi.mjt.news.exceptions.*;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NewsHttpClient {
    //get api key after registration at https://newsapi.org/register
    private final String API_KEY = "put-api-key-here";
    private final String API_ENDPOINT_SCHEME = "https";
    private final String API_ENDPOINT_HOST = "newsapi.org";
    private final String API_ENDPOINT_PATH = "/v2/top-headlines";

    private final Gson GSON = new Gson();

    private final String api_key;
    private final HttpClient httpClient;

    public NewsHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.api_key = API_KEY;
    }

    public NewsHttpClient(HttpClient httpClient, String api_key) {
        this.httpClient = httpClient;
        this.api_key = api_key;
    }

    public Response get(RequestCriteria requestCriteria) throws NewsFeedException {
        HttpResponse<String> httpResponse;

        try {
            URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH,
                    generateParameters(requestCriteria));

            HttpRequest httpRequest = HttpRequest.newBuilder(uri).build();

            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new NewsFeedException("Could not retrieve the articles from news service.", e);
        }

        if (httpResponse.statusCode() == HttpURLConnection.HTTP_OK) {
            return GSON.fromJson(httpResponse.body(), Response.class);
        }

        final int HTTP_TOO_MANY_REQUESTS = 429;

        if (httpResponse.statusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR ||
                httpResponse.statusCode() == HTTP_TOO_MANY_REQUESTS) {
            throw new ClientServiceException("Unavailable service");
        }

        if (httpResponse.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST ||
                httpResponse.statusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new ClientRequestException("Invalid request.");
        }

        throw new NewsFeedException("Unexpected response code.");
    }

    private String generateParameters(RequestCriteria requestCriteria) {
        StringBuilder parameters = new StringBuilder();

        final String PREFIX_COUNTRY = "country=";
        final String PREFIX_CATEGORY = "category=";
        final String PREFIX_KEYWORDS = "q=";
        final String PREFIX_PAGE_SIZE = "pageSize=";
        final String PREFIX_PAGE = "page=";
        final String PREFIX_API_KEY = "apiKey=";

        if (requestCriteria.getCountry() != null) {
            appendPrefixAndValueToFragment(parameters, PREFIX_COUNTRY, requestCriteria.getCountry());
        }

        if (requestCriteria.getCategory() != null) {
            appendPrefixAndValueToFragment(parameters, PREFIX_CATEGORY, requestCriteria.getCategory());
        }

        appendPrefixAndValueToFragment(parameters, PREFIX_KEYWORDS, requestCriteria.getConcatenatedKeywords());

        if (requestCriteria.getPageSize() != null) {
            appendPrefixAndValueToFragment(parameters, PREFIX_PAGE_SIZE, requestCriteria.getPageSize().toString());
        }

        if (requestCriteria.getPage() != null) {
            appendPrefixAndValueToFragment(parameters, PREFIX_PAGE, requestCriteria.getPage().toString());
        }

        appendPrefixAndValueToFragment(parameters, PREFIX_API_KEY, api_key);

        return parameters.toString();
    }

    private void appendPrefixAndValueToFragment(StringBuilder fragment, String prefix, String value) {
        final String AND = "&";

        if (fragment.length() > 0) {
            fragment.append(AND);
        }

        fragment.append(prefix).append(value);
    }
}
