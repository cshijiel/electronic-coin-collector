package com.roc.rest.autoconfigure;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author roc cshijiel@gmail.com
 * @date 2018/1/7 12:52
 */
@ConfigurationProperties(
        prefix = "huobi.config"
)
@Data
public class HuobiRestProperties {
    private String rootUrl;
    private String accessKey;
    private String secretKey;
}
