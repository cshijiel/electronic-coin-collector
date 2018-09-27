package com.roc.rest.autoconfigure;

import com.roc.rest.client.impl.HuobiRestTemplateImpl;
import com.roc.rest.interceptor.LogInterceptor;
import com.roc.rest.service.AccountService;
import com.roc.rest.service.OrderService;
import com.roc.rest.service.impl.AccountServiceImpl;
import com.roc.rest.service.impl.OrderServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

/**
 * @author roc cshijiel@gmail.com
 * @date 2018/1/7 12:50
 */
@Configuration
@ConditionalOnClass(RestTemplate.class)
@EnableConfigurationProperties(HuobiRestProperties.class)
public class HuobiRestAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HuobiRestTemplateImpl restTemplate(RestTemplateBuilder builder, HuobiRestProperties properties, Collection<? extends ClientHttpRequestInterceptor> interceptors) {
        return builder.setConnectTimeout(1000).setReadTimeout(2000)
                .rootUri(properties.getRootUrl())
                .additionalInterceptors(interceptors)
                .build(HuobiRestTemplateImpl.class);
    }

    @Bean
    public ClientHttpRequestInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl();
    }

    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl();
    }

}
