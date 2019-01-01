package cn.com.qjun.commons;

import cn.com.qjun.common.http.HttpClient;
import cn.com.qjun.common.http.HttpClientImpl;
import org.junit.Test;

import java.util.Optional;

/**
 * @author RenQiang
 * @date 2018/12/29
 */
public class HttpClientTest {
    private HttpClient httpClient = new HttpClientImpl();

    @Test
    public void testGetForString() {
        String uri = "http://2018.ip138.com/ic.asp";
        Optional<String> resultOpt = httpClient.getForString(uri);
        resultOpt.ifPresent(System.out::println);
        httpClient.destroy();
    }
}
