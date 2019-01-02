package cn.com.qjun.common.http;

/**
 * Http客户端异常
 *
 * @author RenQiang
 */
public class HttpClientException extends RuntimeException {
    public HttpClientException(String msg) {
        super(msg);
    }

    public HttpClientException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
