package cn.com.qjun.commons.domain;

import lombok.Data;

import java.util.Map;

/**
 * httpbin响应数据
 *
 * @author RenQiang
 * @date 2018/12/29
 */
@Data
public class ResponseData {
    private Map<String, Object> args;
    private Map<String, Object> headers;
    private String origin;
    private String url;
    private Map<String, Object> form;
    private RequestData json;
    private String data;
    private Map<String, String> files;
}
