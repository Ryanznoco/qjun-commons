package cn.com.qjun.common.http;

import org.apache.http.client.methods.HttpGet;

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
     * @param params 查询参数，key-参数名，value-参数值
     * @return 响应结果字符串
     */
    Optional<String> getForString(String uri, Map<String, String> params);

    /**
     * 发送Get请求，返回字符串响应结果
     *
     * @param httpGet 自定义HttpGet对象
     * @return
     */
    Optional<String> getForString(HttpGet httpGet);

    /**
     * 发送Get请求，将响应内容封装为指定对象
     *
     * @param uri   请求地址
     * @param clazz 返回对象Class
     * @param <T>   封装对象类型
     * @return
     */
    <T> Optional<T> getForObject(String uri, Class<T> clazz);

    /**
     * 发送Get请求，将响应内容封装为指定对象
     *
     * @param uri    请求地址
     * @param params 查询参数，key-参数名，value-参数值
     * @param clazz  返回对象Class
     * @param <T>    封装对象类型
     * @return
     */
    <T> Optional<T> getForObject(String uri, Map<String, String> params, Class<T> clazz);

    /**
     * 发送Get请求，将响应内容封装为指定对象
     *
     * @param httpGet 自定义HttpGet对象
     * @param clazz   返回对象Class
     * @param <T>     封装对象类型
     * @return
     */
    <T> Optional<T> getForObject(HttpGet httpGet, Class<T> clazz);

    /**
     * 释放资源，应用关闭时调用
     */
    void destroy();
}
