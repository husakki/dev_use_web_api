package com.secure_use_api.rest.middleware;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.secure_use_api.exceptions.ExceptionResponse;
import com.secure_use_api.rest.service.UserService;
import com.secure_use_api.security.UserAuthentication;

@Component
public class ResourceLimitMiddleware implements HandlerInterceptor {

    // Is used to resource limit controller methods with this annotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ResourceLimited {}

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException, IOException {

        System.out.println("ResourceLimitMiddleware start");

        if (handler != null && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ResourceLimited performResourceLimit = method.getAnnotation(ResourceLimited.class);

            if (performResourceLimit != null) {
                UserAuthentication user = (UserAuthentication) SecurityContextHolder.getContext()
                        .getAuthentication();

                long resourceLimitLeft = userService.getResourceLimitLeft(user.getUserId());

                if (resourceLimitLeft <= 0) {
                    ExceptionResponse exRes = new ExceptionResponse(HttpStatus.FORBIDDEN,
                            "Daily ResourceLimit reached",
                            request.getRequestURI());

                    exRes.writeToResponse(response, request);

                    return false;
                }

                long preTimestampInSeconds = Instant.now().getEpochSecond();

                request.setAttribute("ResourceLimitMiddleware__resourceLimitLeft", resourceLimitLeft);
                request.setAttribute("ResourceLimitMiddleware__preTimestampInSeconds", preTimestampInSeconds);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        UserAuthentication user = (UserAuthentication) SecurityContextHolder.getContext()
                .getAuthentication();
        Long resourceLimitLeft = (Long) request.getAttribute("ResourceLimitMiddleware__resourceLimitLeft");
        Long preTimestampInSeconds = (Long) request.getAttribute("ResourceLimitMiddleware__preTimestampInSeconds");

        if (resourceLimitLeft != null && preTimestampInSeconds != null) {
            long postTimestampInSeconds = Instant.now().getEpochSecond();

            userService.setResourceLimitLeft(user.getUserId(),
                    resourceLimitLeft - (postTimestampInSeconds - preTimestampInSeconds));
        }

        System.out.println("ResourceLimitMiddleware end");
    }
}