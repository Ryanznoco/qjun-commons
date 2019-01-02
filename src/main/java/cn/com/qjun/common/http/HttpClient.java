package cn.com.qjun.common.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.Map;
import java.util.Optional;

/**
 * @author RenQiang
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
     * @return 响应结果字符串
     */
    Optional<String> getForString(HttpGet httpGet);

    /**
     * 发送Get请求，将响应内容封装为指定对象
     *
     * @param uri   请求地址
     * @param clazz 返回对象Class
     * @param <T>   封装对象类型
     * @return 响应体封装的Java对象
     */
    <T> Optional<T> getForObject(String uri, Class<T> clazz);

    /**
     * 发送Get请求，将响应内容封装为指定对象
     *
     * @param uri    请求地址
     * @param params 查询参数，key-参数名，value-参数值
     * @param clazz  返回对象Class
     * @param <T>    封装对象类型
     * @return 响应体封装的Java对象
     */
    <T> Optional<T> getForObject(String uri, Map<String, String> params, Class<T> clazz);

    /**
     * 发送Get请求，将响应内容封装为指定对象
     *
     * @param httpGet 自定义HttpGet对象
     * @param clazz   返回对象Class
     * @param <T>     封装对象类型
     * @return 响应体封装的Java对象
     */
    <T> Optional<T> getForObject(HttpGet httpGet, Class<T> clazz);

    /**
     * 发送Post请求，返回字符串响应结果
     *
     * @param uri 请求地址
     * @return 响应结果字符串
     */
    Optional<String> postForString(String uri);

    /**
     * 发送Post请求，返回字符串响应结果
     *
     * @param uri    请求地址
     * @param params Post参数，key-参数名，value-参数值
     * @return 响应结果字符串
     */
    Optional<String> postForString(String uri, Map<String, String> params);

    /**
     * 发送Post请求，返回字符串响应结果
     *
     * @param httpPost 自定义HttpPost对象
     * @return 响应结果字符串
     */
    Optional<String> postForString(HttpPost httpPost);

    /**
     * 发送Post请求，返回字符串响应结果
     *
     * @param uri  请求地址
     * @param body 请求体，转换成Json提交
     * @return 响应结果字符串
     */
    Optional<String> postForString(String uri, Object body);

    /**
     * 发送Post请求，将响应内容封装为指定对象
     *
     * @param uri   请求地址
     * @param clazz 返回对象Class
     * @param <T>   封装对象类型
     * @return 响应体封装的Java对象
     */
    <T> Optional<T> postForObject(String uri, Class<T> clazz);

    /**
     * 发送Post请求，将响应内容封装为指定对象
     *
     * @param uri    请求地址
     * @param params Post参数，key-参数名，value-参数值
     * @param clazz  返回对象Class
     * @param <T>    封装对象类型
     * @return 响应体封装的Java对象
     */
    <T> Optional<T> postForObject(String uri, Map<String, String> params, Class<T> clazz);

    /**
     * 发送Post请求，将响应内容封装为指定对象
     *
     * @param httpPost 自定义HttpPost对象
     * @param clazz    返回对象Class
     * @param <T>      封装对象类型
     * @return 响应体封装的Java对象
     */
    <T> Optional<T> postForObject(HttpPost httpPost, Class<T> clazz);

    /**
     * 发送Post请求，返回字符串响应结果
     *
     * @param uri   请求地址
     * @param body  请求体，转换成Json提交
     * @param clazz 返回对象Class
     * @param <T>   封装对象类型
     * @return 响应体封装的Java对象
     */
    <T> Optional<T> postForObject(String uri, Object body, Class<T> clazz);

    /**
     * 释放资源，应用关闭时调用
     */
    void destroy();
}
