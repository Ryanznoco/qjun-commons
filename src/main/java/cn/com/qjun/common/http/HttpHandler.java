package cn.com.qjun.common.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Http帮助类，为高并发做了优化，重用连接
 *
 * @author RenQiang
 * @date 2018/12/29
 */
@Slf4j
public class HttpHandler {
    private final HttpClientConfig config;
    private volatile boolean running;
    /**
     * 单例Http客户端
     */
    private final CloseableHttpClient httpClient;
    /**
     * 连接管理器
     */
    private final PoolingHttpClientConnectionManager connectionManager;

    public HttpHandler(HttpClientConfig config) {
        this.config = config;
        this.running = true;
        this.connectionManager = initConnectionManager(config);
        /**
         * 连接保活策略
         */
        ConnectionKeepAliveStrategy keepAliveStrategy = initKeepAliveStrategy(config);
        this.httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(keepAliveStrategy)
                .build();
        /**
         * 活动连接监控线程，自动关闭无效连接
         */
        Thread monitorThread = new Thread(new IdleConnectionMonitor());
        monitorThread.setName("QJ-HTTP-MT");
        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    public <T> T doRequest(HttpUriRequest request, ResponseHandler<T> responseHandler) throws IOException {
        return httpClient.execute(request, responseHandler);
    }

    /**
     * 关闭Http客户端
     */
    public void shutdown() {
        this.running = false;
        try {
            this.connectionManager.shutdown();
            this.httpClient.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private ConnectionKeepAliveStrategy initKeepAliveStrategy(HttpClientConfig config) {
        return (httpResponse, httpContext) -> {
            HeaderElementIterator it = new BasicHeaderElementIterator(
                    httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String name = he.getName();
                String value = he.getValue();
                if ("timeout".equalsIgnoreCase(name) && value != null) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch (NumberFormatException ignore) {
                    }
                }
            }
            return config.getDefaultKeepAlive();
        };
    }

    private PoolingHttpClientConnectionManager initConnectionManager(HttpClientConfig config) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(config.getMaxTotalConnection());
        connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionPerRoute());
        return connectionManager;
    }

    /**
     * 活动连接监控线程
     */
    class IdleConnectionMonitor implements Runnable {

        @Override
        public void run() {
            log.debug("-------HttpClient connection monitor thread start-------");
            while (running) {
                try {
                    Thread.sleep(config.getConnectionCheckPeriod() * 1000);
                    // 关闭过期连接
                    connectionManager.closeExpiredConnections();
                    // 关闭空闲时间超时的连接
                    connectionManager.closeIdleConnections(config.getConnectionMaxIdleTime(), TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
            log.debug("-------HttpClient connection monitor thread shutdown-------");
        }
    }
}
