package com.lagou.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@Component
public class WebAppConfigurer extends WebMvcConfigurationSupport {

    /**
     * 添加自定义拦截器配置
     *
     * @param registry InterceptorRegistry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TransPortIPFilter()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
