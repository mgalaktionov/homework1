package ru.digitalhabbits.homework1.service;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.slf4j.LoggerFactory.getLogger;


public class WikipediaClient {
    public static final String WIKIPEDIA_SEARCH_URL = "https://en.wikipedia.org/w/api.php";
    private final Logger logger = getLogger(WikipediaClient.class);
    private static final int SUCCESS_HTTP_STATUS_CODE = 200;

    @Nonnull
    public String search(@Nonnull String searchString) {

        //Creating get request to wikipedia api
        HttpGet getReq = prepareHttpGet(prepareSearchUrl(searchString));
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            logger.info("Request url = '{}", prepareSearchUrl(searchString));
            CloseableHttpResponse response;
            response = client.execute(getReq);
            try {
                // Checking response status

                if (response != null && getResponseCode(response) == SUCCESS_HTTP_STATUS_CODE) {
                    String json = entityToString(response.getEntity());
                    JSONArray textsFound = JsonPath.read(json, "$.query.pages.*.extract");
                    StringBuilder builder = new StringBuilder();
                    for (Object object : textsFound) {
                        builder.append(object.toString())
                                .append(" ");
                    }
                    String text = builder.toString();
                    return text.replaceAll("\\\\n", "\n");
                } else {
                    logger.error("Something went wrong with response, program will exit");
                    System.exit(1);
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            logger.info("IO exception was caught, system exit");
            e.printStackTrace();
            System.exit(1);
        }
        return "";
    }

    private int getResponseCode(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    private String entityToString(@Nonnull HttpEntity entity) throws IOException {
        return EntityUtils.toString(entity);
    }

    @Nonnull
    private HttpGet prepareHttpGet(@Nonnull URI requestUri) {
        return new HttpGet(requestUri);
    }

    @Nonnull
    private URI prepareSearchUrl(@Nonnull String searchString) {
        try {
            return new URIBuilder(WIKIPEDIA_SEARCH_URL)
                    .addParameter("action", "query")
                    .addParameter("format", "json")
                    .addParameter("titles", searchString)
                    .addParameter("prop", "extracts")
                    .addParameter("explaintext", "")
                    .build();
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }
}
