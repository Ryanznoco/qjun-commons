package cn.com.qjun.common.http;

import java.util.Map;
import java.util.Optional;

/**
 * @author RenQiang
 * @date 2018/12/29
 */
public interface HttpClient {

    /**
     * 发送Get请求，返回字符串响应结果
     *
     * @param uri 请求地址
     * @return 响应结果字符串
     */
    Optional<String> getForString(String uri);

    /**
     * 发送Get请求，返回字符串响应结果
     *
     * @param uri    请求地址
     * @param params 请求参数
     * @return 响应结果字符串
     */
    Optional<String> getForString(String uri, Map<String, String> params);

    /**
     * 释放资源，应用关闭时调用
     */
    void destroy();
}
