package com.maxcode.buyer.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public Map<String, Object> handleError(HttpServletRequest request) {
        Map<String, Object> errorInfo = new HashMap<>();
        
        // 获取原始错误信息
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        
        errorInfo.put("timestamp", new Date());
        errorInfo.put("status", status != null ? status : "N/A");
        errorInfo.put("error", exception != null ? exception.getClass().getName() : "Unknown Error");
        errorInfo.put("message", exception != null ? ((Exception) exception).getMessage() : "No message available");
        errorInfo.put("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

        // 打印堆栈跟踪
        if (exception instanceof Exception) {
            ((Exception) exception).printStackTrace();
        }

        return errorInfo;
    }

    public String getErrorPath() {
        return "/error";
    }
} 