package cn.com.qjun.common.http;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

/**
 * @author RenQiang
 * @date 2018/12/29
 */
@Slf4j
public class HttpClientImpl implements HttpClient {
    private final HttpClientConfig config;
    private final HttpHandler httpHandler;

    public HttpClientImpl() {
        this(new HttpClientConfig());
    }

    public HttpClientImpl(HttpClientConfig config) {
        this.config = config;
        this.httpHandler = new HttpHandler(config);
    }

    @Override
    public Optional<String> getForString(@NonNull String uri) {
        HttpGet httpGet = new HttpGet(uri);
        try {
            return Optional.ofNullable(httpHandler.doRequest(httpGet,
                    response -> EntityUtils.toString(response.getEntity(), config.getCharset())));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<String> getForString(@NonNull String uri, @NonNull Map<String, String> params) {
        try {
            URIBuilder builder = new URIBuilder(uri);
            params.forEach(builder::addParameter);
            HttpGet httpGet = new HttpGet(builder.build());
            return Optional.ofNullable(httpHandler.doRequest(httpGet,
                    response -> EntityUtils.toString(response.getEntity(), config.getCharset())));
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    @Override
    public <T> Optional<T> getForObject(@NonNull String uri, @NonNull Class<T> clazz) {
        Optional<String> responseOpt = getForString(uri);
        if (responseOpt.isPresent()) {
            return responseOpt.map(response -> {
                try {
                    return config.getObjectMapper().readValue(response, clazz);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    throw new HttpClientException(e.getMessage(), e);
                }
            });
        }
        return Optional.empty();
    }

    @Override
    public void destroy() {
        httpHandler.shutdown();
        log.info("-------HttpClient Shutdown-------");
    }
}
