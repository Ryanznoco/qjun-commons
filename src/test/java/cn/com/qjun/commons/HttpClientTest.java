package cn.com.qjun.commons;

import cn.com.qjun.common.http.HttpClient;
import cn.com.qjun.common.http.HttpClientImpl;
import cn.com.qjun.commons.domain.LoginForm;
import cn.com.qjun.commons.domain.RequestData;
import cn.com.qjun.commons.domain.ResponseData;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author RenQiang
 */
public class HttpClientTest {
    private static final String BASE_URL = "http://prod.qjun.com.cn:7780";
    private HttpClient httpClient = new HttpClientImpl();

    @Test
    public void testUTF8() {
        String uri = "http://localhost:8082/auth/login";
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("中文乱码");
        loginForm.setPassword("123456abcdef.");
        loginForm.setVerifyCode("123456");
        Optional<String> responseOpt = httpClient.postForString(uri, loginForm);
        Assert.assertTrue(responseOpt.isPresent());
    }

    @Test
    public void testGetForString() {
        String getURI = BASE_URL + "/get";
        Optional<String> responseOpt = httpClient.getForString(getURI);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertTrue(responseOpt.get().contains(getURI));

        Map<String, String> params = new HashMap<>(8);
        params.put("key1", "value1");
        params.put("key2", "value2");
        params.put("key3", "value3");
        responseOpt = httpClient.getForString(getURI, params);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertTrue(responseOpt.get().contains("key1"));
        Assert.assertTrue(responseOpt.get().contains("value3"));

        HttpGet httpGet = new HttpGet(getURI + "?param1=value1&param2=value2");
        httpGet.addHeader("header", "header_value");
        responseOpt = httpClient.getForString(httpGet);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertTrue(responseOpt.get().contains("header_value"));
        Assert.assertTrue(responseOpt.get().contains("value2"));
    }

    @Test
    public void testGetForObject() {
        String getURI = BASE_URL + "/get";
        Optional<ResponseData> responseOpt = httpClient.getForObject(getURI, ResponseData.class);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertEquals(responseOpt.get().getUrl(), getURI);

        Map<String, String> params = new HashMap<>(8);
        params.put("key1", "value1");
        params.put("key2", "value2");
        params.put("key3", "value3");
        responseOpt = httpClient.getForObject(getURI, params, ResponseData.class);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertEquals("value1", responseOpt.get().getArgs().get("key1"));
        Assert.assertEquals("value3", responseOpt.get().getArgs().get("key3"));

        HttpGet httpGet = new HttpGet(getURI + "?param1=value1&param2=value2");
        httpGet.addHeader("Simple-Header", "header_value");
        responseOpt = httpClient.getForObject(httpGet, ResponseData.class);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertEquals("header_value", responseOpt.get().getHeaders().get("Simple-Header"));
        Assert.assertEquals("value2", responseOpt.get().getArgs().get("param2"));
    }

    @Test
    public void testPostForString() {
        String postURI = BASE_URL + "/post";
        Optional<String> responseOpt = httpClient.postForString(postURI);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertTrue(responseOpt.get().contains(postURI));

        Map<String, String> params = new HashMap<>(8);
        params.put("key1", "value1");
        params.put("key2", "value2");
        params.put("key3", "value3");
        responseOpt = httpClient.postForString(postURI, params);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertTrue(responseOpt.get().contains("key1"));
        Assert.assertTrue(responseOpt.get().contains("value3"));
        Assert.assertTrue(responseOpt.get().contains("application/x-www-form-urlencoded"));

        HttpPost httpPost = new HttpPost(postURI + "?param1=value1&param2=value2");
        httpPost.addHeader("header", "header_value");
        httpPost.setEntity(new StringEntity("hahahaha", "UTF8"));
        responseOpt = httpClient.postForString(httpPost);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertTrue(responseOpt.get().contains("header_value"));
        Assert.assertTrue(responseOpt.get().contains("value2"));
        Assert.assertTrue(responseOpt.get().contains("hahahaha"));

        RequestData requestData = RequestData.builder().name("zhangsan").age(12).nice(false).build();
        responseOpt = httpClient.postForString(postURI, requestData);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertTrue(responseOpt.get().contains("zhangsan"));
        Assert.assertTrue(responseOpt.get().contains("12"));
        Assert.assertTrue(responseOpt.get().contains("false"));
        Assert.assertTrue(responseOpt.get().contains("application/json"));
    }

    @Test
    public void testPostForObject() {
        String postURI = BASE_URL + "/post";
        Optional<ResponseData> responseOpt = httpClient.postForObject(postURI, ResponseData.class);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertEquals(responseOpt.get().getUrl(), postURI);

        Map<String, String> params = new HashMap<>(8);
        params.put("key1", "value1");
        params.put("key2", "value2");
        params.put("key3", "value3");
        responseOpt = httpClient.postForObject(postURI, params, ResponseData.class);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertEquals("value1", responseOpt.get().getForm().get("key1"));
        Assert.assertEquals("value3", responseOpt.get().getForm().get("key3"));
        Assert.assertEquals("application/x-www-form-urlencoded", responseOpt.get().getHeaders().get("Content-Type"));

        HttpPost httpPost = new HttpPost(postURI + "?param1=value1&param2=value2");
        httpPost.addHeader("Test-Header", "header_value");
        httpPost.setEntity(new StringEntity("hahahaha", "UTF8"));
        responseOpt = httpClient.postForObject(httpPost, ResponseData.class);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertEquals("header_value", responseOpt.get().getHeaders().get("Test-Header"));
        Assert.assertEquals("value2", responseOpt.get().getArgs().get("param2"));
        Assert.assertEquals("hahahaha", responseOpt.get().getData());

        RequestData requestData = RequestData.builder().name("zhangsan").age(12).nice(false).build();
        responseOpt = httpClient.postForObject(postURI, requestData, ResponseData.class);
        Assert.assertTrue(responseOpt.isPresent());
        System.out.println(responseOpt.get());
        Assert.assertEquals("zhangsan", responseOpt.get().getJson().getName());
        Assert.assertEquals(12, (int) responseOpt.get().getJson().getAge());
        Assert.assertEquals(false, responseOpt.get().getJson().getNice());
        Assert.assertEquals("application/json", responseOpt.get().getHeaders().get("Content-Type"));
    }
}
