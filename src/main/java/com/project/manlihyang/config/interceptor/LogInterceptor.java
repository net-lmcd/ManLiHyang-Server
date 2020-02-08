package com.project.manlihyang.config.interceptor;

import com.project.manlihyang.util.ApiHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    private ApiHelper helper;
    public LogInterceptor(ApiHelper helper) {
        this.helper = helper;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String path = request.getServletPath();
       // Map<String, Object> map = helper.convert(request);
        byte[] byteRequest = StreamUtils.copyToByteArray(request.getInputStream());
        Object obj = helper.read(byteRequest);
        helper.print(obj);

        helper.print("[" + method + "] " + path);
        return true;
    }
}
