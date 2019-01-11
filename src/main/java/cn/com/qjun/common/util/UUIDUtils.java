package cn.com.qjun.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Ryan
 * @date 2018-12-27
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDUtils {

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String standard() {
        return UUID.randomUUID().toString();
    }
}
