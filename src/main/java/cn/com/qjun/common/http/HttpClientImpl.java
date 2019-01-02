package cn.com.qjun.common.http;

import cn.com.qjun.common.util.URIUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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
            URI requestUri = URIUtils.buildUriWithParams(uri, params);
            HttpGet httpGet = new HttpGet(requestUri);
            return Optional.ofNullable(httpHandler.doRequest(httpGet,
                    response -> EntityUtils.toString(response.getEntity(), config.getCharset())));
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<String> getForString(@NonNull HttpGet httpGet) {
        try {
            return Optional.ofNullable(httpHandler.doRequest(httpGet,
                    response -> EntityUtils.toString(response.getEntity(), config.getCharset())));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    @Override
    public <T> Optional<T> getForObject(@NonNull String uri, @NonNull Class<T> clazz) {
        Optional<String> responseOpt = getForString(uri);
        return responseOpt.map(new JsonToObjectMapper<>(config.getObjectMapper(), clazz));
    }

    @Override
    public <T> Optional<T> getForObject(@NonNull String uri, @NonNull Map<String, String> params, @NonNull Class<T> clazz) {
        Optional<String> responseOpt = getForString(uri, params);
        return responseOpt.map(new JsonToObjectMapper<>(config.getObjectMapper(), clazz));
    }

    @Override
    public <T> Optional<T> getForObject(@NonNull HttpGet httpGet, @NonNull Class<T> clazz) {
        Optional<String> responseOpt = getForString(httpGet);
        return responseOpt.map(new JsonToObjectMapper<>(config.getObjectMapper(), clazz));
    }

    @Override
    public void destroy() {
        httpHandler.shutdown();
        log.info("-------HttpClient Shutdown-------");
    }

    /**
     * The mapper convert json to object
     *
     * @param <T> object type
     */
    class JsonToObjectMapper<T> implements Function<String, T> {
        private ObjectMapper objectMapper;
        private Class<T> clazz;

        JsonToObjectMapper(ObjectMapper objectMapper, Class<T> clazz) {
            this.objectMapper = objectMapper;
            this.clazz = clazz;
        }

        @Override
        public T apply(String json) {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new HttpClientException(e.getMessage(), e);
            }
        }
    }
}
