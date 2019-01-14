package cn.com.qjun.common.constant;

import lombok.Getter;

/**
 * @author RenQiang
 */
@Getter
public enum HttpRequestHeader {
    /**
     * 用户代理
     */
    UserAgent("User-Agent");

    private String value;

    HttpRequestHeader(String value) {
        this.value = value;
    }
}
