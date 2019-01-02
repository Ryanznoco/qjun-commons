package cn.com.qjun.commons.domain;

import lombok.Builder;
import lombok.Data;

/**
 * Post请求体
 *
 * @author RenQiang
 * @date 2018/12/29
 */
@Data
@Builder
public class RequestData {
    private String name;
    private Integer age;
    private Boolean nice;
}
