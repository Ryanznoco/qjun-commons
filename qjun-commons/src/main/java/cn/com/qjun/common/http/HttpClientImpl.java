package cn.com.qjun.common.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
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
    public Optional<String> getForString(String uri) {
        HttpGet httpGet = new HttpGet(uri);
        try {
            return Optional.ofNullable(httpHandler.doRequest(httpGet,
                    (response) -> EntityUtils.toString(response.getEntity(), config.getCharset())));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getForString(String uri, Map<String, String> params) {
        return null;
    }

    @Override
    public void destroy() {
        httpHandler.shutdown();
        log.info("-------HttpClient Shutdown-------");
    }
}
