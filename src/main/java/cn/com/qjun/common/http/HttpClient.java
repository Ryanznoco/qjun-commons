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
     * 发送Get请求，将响应内容封装为指定对象
     *
     * @param uri
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Optional<T> getForObject(String uri, Class<T> clazz);

    /**
     * 释放资源，应用关闭时调用
     */
    void destroy();
}
