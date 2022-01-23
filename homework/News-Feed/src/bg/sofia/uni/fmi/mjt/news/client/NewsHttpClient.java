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
    private static final String API_KEY = "put-api-key-here";
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "newsapi.org";
    private static final String API_ENDPOINT_PATH = "/v2/top-headlines";

    private static final Gson GSON = new Gson();

    private final String apiKey;
    private final HttpClient httpClient;

    public NewsHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.apiKey = API_KEY;
    }

    public NewsHttpClient(HttpClient httpClient, String apiKey) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
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

        final int httpTooManyRequests = 429;

        if (httpResponse.statusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR ||
                httpResponse.statusCode() == httpTooManyRequests) {
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

        final String prefixCountry = "country=";
        final String prefixCategory = "category=";
        final String prefixKeywords = "q=";
        final String prefixPageSize = "pageSize=";
        final String prefixPage = "page=";
        final String prefixApiKey = "apiKey=";

        if (requestCriteria.getCountry() != null) {
            appendPrefixAndValueToFragment(parameters, prefixCountry, requestCriteria.getCountry());
        }

        if (requestCriteria.getCategory() != null) {
            appendPrefixAndValueToFragment(parameters, prefixCategory, requestCriteria.getCategory());
        }

        appendPrefixAndValueToFragment(parameters, prefixKeywords, requestCriteria.getConcatenatedKeywords());

        if (requestCriteria.getPageSize() != null) {
            appendPrefixAndValueToFragment(parameters, prefixPageSize, requestCriteria.getPageSize().toString());
        }

        if (requestCriteria.getPage() != null) {
            appendPrefixAndValueToFragment(parameters, prefixPage, requestCriteria.getPage().toString());
        }

        appendPrefixAndValueToFragment(parameters, prefixApiKey, apiKey);

        return parameters.toString();
    }

    private void appendPrefixAndValueToFragment(StringBuilder fragment, String prefix, String value) {
        final String and = "&";

        if (fragment.length() > 0) {
            fragment.append(and);
        }

        fragment.append(prefix).append(value);
    }
}
