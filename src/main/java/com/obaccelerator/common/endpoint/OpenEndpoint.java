package com.obaccelerator.common.endpoint;

import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Getter
public enum OpenEndpoint {

    POST_TOKEN(RequestMethod.POST.name(), "/token");

    private final String method;
    private final String path;

    OpenEndpoint(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public static boolean isOpenEndpoint(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        for (OpenEndpoint openEndpoint : OpenEndpoint.values()) {
            if (requestUri.contains(openEndpoint.getPath()) && method.equals(openEndpoint.getMethod())) {
                return true;
            }
        }
        return false;
    }
}
