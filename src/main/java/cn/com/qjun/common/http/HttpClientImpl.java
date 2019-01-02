package cn.com.qjun.common.http;

import cn.com.qjun.common.util.URIUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
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
            return Optional.ofNullable(httpHandler.doRequest(httpGet, new StringResponseHandler()));
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
            return Optional.ofNullable(httpHandler.doRequest(httpGet, new StringResponseHandler()));
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<String> getForString(@NonNull HttpGet httpGet) {
        try {
            return Optional.ofNullable(httpHandler.doRequest(httpGet, new StringResponseHandler()));
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
            return Optional.ofNullable(httpHandler.doRequest(httpPost, new StringResponseHandler()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<String> postForString(@NonNull String uri, @NonNull Map<String, String> params) {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        List<NameValuePair> paramList = new ArrayList<>();
        params.forEach((key, value) -> paramList.add(new BasicNameValuePair(key, value)));
        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramList, config.getCharset());
            httpPost.setEntity(formEntity);
            return Optional.ofNullable(httpHandler.doRequest(httpPost, new StringResponseHandler()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<String> postForString(@NonNull HttpPost httpPost) {
        try {
            return Optional.ofNullable(httpHandler.doRequest(httpPost, new StringResponseHandler()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<String> postForString(@NonNull String uri, @NonNull Object body) {
        try {
            String json = config.getObjectMapper().writeValueAsString(body);
            HttpPost httpPost = new HttpPost(uri);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            return Optional.ofNullable(httpHandler.doRequest(httpPost, new StringResponseHandler()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    @Override
    public <T> Optional<T> postForObject(@NonNull String uri, @NonNull Class<T> clazz) {
        Optional<String> responseOpt = postForString(uri);
        return responseOpt.map(new JsonToObjectMapper<>(config.getObjectMapper(), clazz));
    }

    @Override
    public <T> Optional<T> postForObject(@NonNull String uri, @NonNull Map<String, String> params, @NonNull Class<T> clazz) {
        Optional<String> responseOpt = postForString(uri, params);
        return responseOpt.map(new JsonToObjectMapper<>(config.getObjectMapper(), clazz));
    }

    @Override
    public <T> Optional<T> postForObject(@NonNull HttpPost httpPost, @NonNull Class<T> clazz) {
        Optional<String> responseOpt = postForString(httpPost);
        return responseOpt.map(new JsonToObjectMapper<>(config.getObjectMapper(), clazz));
    }

    @Override
    public <T> Optional<T> postForObject(@NonNull String uri, @NonNull Object body, @NonNull Class<T> clazz) {
        Optional<String> responseOpt = postForString(uri, body);
        return responseOpt.map(new JsonToObjectMapper<>(config.getObjectMapper(), clazz));
    }

    @Override
    public void destroy() {
        httpHandler.shutdown();
        log.info("-------HttpClient Shutdown-------");
    }

    private String buildStatusErrorMsg(int statusCode) {
        return String.format("Response status code is not ok %d", statusCode);
    }

    class StringResponseHandler implements ResponseHandler<String> {

        @Override
        public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == Consts.STATUS_CODE_OK) {
                return EntityUtils.toString(httpResponse.getEntity(), config.getCharset());
            }
            throw new HttpClientException(buildStatusErrorMsg(statusCode));
        }
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
