package com.melik.mdc.config;

import com.melik.mdc.interceptor.MDCHandlerInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author mselvi
 * @Created 09.10.2023
 */

@Component
public class WebMvcConfig implements WebMvcConfigurer {

    private final MDCHandlerInterceptor mdcHandlerInterceptor;

    public WebMvcConfig(MDCHandlerInterceptor mdcHandlerInterceptor) {
        this.mdcHandlerInterceptor = mdcHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mdcHandlerInterceptor);
    }
}
