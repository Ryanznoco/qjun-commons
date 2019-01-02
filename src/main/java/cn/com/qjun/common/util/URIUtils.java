package cn.com.qjun.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author RenQiang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class URIUtils {

    /**
     * 为URI拼接查询参数
     *
     * @param uri    URI
     * @param params 查询参数，key-参数名，value-参数值
     * @return 带查询参数的URI
     * @throws URISyntaxException Checked exception thrown to indicate that a string could not be parsed as a URI reference.
     */
    public static URI buildUriWithParams(@NonNull String uri, @NonNull Map<String, String> params) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(uri);
        params.forEach(builder::addParameter);
        return builder.build();
    }
}
