package cn.com.qjun.common.http;

import lombok.Data;

/**
 * @author RenQiang
 * @date 2018/12/29
 */
@Data
public class HttpClientConfig {
    /**
     * 连接保持时间，单位：秒
     * 默认每个连接最多保持60秒
     */
    private int defaultKeepAlive = 60;
    /**
     * 最大总连接数
     */
    private int maxTotalConnection = 100;
    /**
     * 每路由最大连接数
     */
    private int maxConnectionPerRoute = 10;
    /**
     * 空闲连接检查周期，单位：秒
     * 默认每5秒检查一次
     */
    private int connectionCheckPeriod = 5;
    /**
     * 连接最大空闲时间，单位：秒
     * 默认空闲超过30秒则关闭连接
     */
    private int connectionMaxIdleTime = 30;

}
