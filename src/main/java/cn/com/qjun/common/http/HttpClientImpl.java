package cn.com.qjun.common.http;

import cn.com.qjun.common.util.URIUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
    private static final int STATUS_CODE_OK = 200;

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
            return Optional.ofNullable(httpHandler.doRequest(httpGet, response -> {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == STATUS_CODE_OK) {
                    return EntityUtils.toString(response.getEntity(), config.getCharset());
                }
                throw new HttpClientException(String.format("Response status code is not ok. %d", statusCode));
            }));
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
    public Optional<String> postForString(@NonNull String uri) {
        HttpPost httpPost = new HttpPost(uri);
        try {
            return Optional.ofNullable(httpHandler.doRequest(httpPost,
                    response -> {
                if (response.getStatusLine().getStatusCode() == 200)
                        return EntityUtils.toString(response.getEntity());
                    }));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<String> postForString(String uri, Map<String, String> params) {
        return Optional.empty();
    }

    @Override
    public Optional<String> postForString(HttpPost httpPost) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<String> postForString(String uri, T body, Class<T> bodyClass) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> postForObject(String uri, Class<T> clazz) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> postForObject(String uri, Map<String, String> params, Class<T> clazz) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> postForObject(HttpPost httpPost, Class<T> clazz) {
        return Optional.empty();
    }

    @Override
    public <BT, RT> Optional<RT> postForString(String uri, BT body, Class<BT> bodyClass, Class<RT> resultClass) {
        return Optional.empty();
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
