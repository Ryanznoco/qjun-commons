package cn.com.qjun.common.http;

import java.util.Map;
import java.util.Optional;

/**
 * @author RenQiang
 * @date 2018/12/29
 */
public interface HttpClient {

    Optional<String> getAsString(String uri);

    Optional<String> getAsString(String uri, Map<String, String> params);

    void destroy();
}
