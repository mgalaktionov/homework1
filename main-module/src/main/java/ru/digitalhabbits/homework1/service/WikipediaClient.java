package ru.digitalhabbits.homework1.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import static org.slf4j.LoggerFactory.getLogger;


public class WikipediaClient {
    private final int SUCCESS_HTTP_STATUS_CODE = 200;
    public static final String WIKIPEDIA_SEARCH_URL = "https://en.wikipedia.org/w/api.php";
    private final Logger logger = getLogger(WikipediaClient.class);

    @Nonnull
    public String search(@Nonnull String searchString) throws IOException {
        final URI uri = prepareSearchUrl(searchString);
        // TODO: NotImplemented
        //Creating get request to wikipedia api
        HttpGet getReq = prepareHttpGet(prepareSearchUrl(searchString));
        CloseableHttpClient client = HttpClients.createDefault();
        logger.info("Request url = "+prepareSearchUrl(searchString).toString());
        CloseableHttpResponse response;
        try {
            response = client.execute(getReq);
            try{
                // Cheking response status
                if (response != null && getResponseCode(response) == SUCCESS_HTTP_STATUS_CODE) {
                    return entityToString(response.getEntity());
                } else {
                    logger.error("Something went wrong with response, programm will exit");
                    System.exit(1);
                }
            }finally {
                response.close();
            }
        } catch (IOException e) {
            logger.info("IO exception was caught, system exit");
            e.printStackTrace();
            System.exit(1);
        }finally {
            client.close();
        }
        return "";
    }

    private int getResponseCode(CloseableHttpResponse response){
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
                    .addParameter("explaintext","")
                    .build();
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }
}
