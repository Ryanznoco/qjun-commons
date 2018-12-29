package cn.com.qjun.common.http;

import java.util.Map;
import java.util.Optional;

/**
 * @author RenQiang
 * @date 2018/12/29
 */
public interface HttpClient {

    Optional<String> getForString(String uri);

    Optional<String> getForString(String uri, Map<String, String> params);

    void destroy();
}
