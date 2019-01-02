package cn.com.qjun.commons.domain;

import lombok.Builder;
import lombok.Data;

/**
 * Post请求体
 *
 * @author RenQiang
 */
@Data
@Builder
public class RequestData {
    private String name;
    private Integer age;
    private Boolean nice;
}
