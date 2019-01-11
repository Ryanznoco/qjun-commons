package cn.com.qjun.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author RenQiang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDUtils {

    /**
     * 生成32位UUID
     *
     * @return 32位UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成36位标准UUID
     *
     * @return 36位标准UUID
     */
    public static String standard() {
        return UUID.randomUUID().toString();
    }
}
