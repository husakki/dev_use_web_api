package org.tzi.use.api_security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.tzi.use.api_security.rest.middleware.AuthorizationMiddleware;
import org.tzi.use.api_security.rest.middleware.ResourceLimitMiddleware;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthorizationMiddleware authorizationMiddleware;

    @Autowired
    private ResourceLimitMiddleware resourceLimitMiddleware;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationMiddleware);

        registry.addInterceptor(resourceLimitMiddleware)
                .addPathPatterns("/user/**");

        // Register the ResourceLimitMiddleware
        // registry.addInterceptor(resourceLimitMiddleware)
        // .addPathPatterns("/api/**") // Specify URL patterns for resource limit
        // .order(1); // Ensure it runs after AuthorizationMiddleware
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Simply setting Access-Control Headers
        registry.addMapping("/**")
                // .allowedOrigins("https://example.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(false);
    }
}