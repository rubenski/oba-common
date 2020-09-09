package com.obaccelerator.common.endpoint;

import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clients don't have to provide a token on open endpoints
 */
@Getter
public enum OpenEndpoint {

    GET_TOKENS(RequestMethod.GET.name(), Pattern.compile("^/tokens$")),
    POST_ORGANIZATIONS(RequestMethod.POST.name(), Pattern.compile("^/organizations$")),
    POST_SESSIONS(RequestMethod.POST.name(), Pattern.compile("^/sessions$")),
    GET_LOGOS(RequestMethod.GET.name(), Pattern.compile("^/logos/[A-Za-z0-9-_+%]+?\\.png$"));

    private final String method;
    private final Pattern pattern;

    OpenEndpoint(String method, Pattern pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    public static boolean isOpenEndpoint(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        for (OpenEndpoint endpoint : OpenEndpoint.values()) {
            if (method.equals(endpoint.getMethod())) {
                Matcher matcher = endpoint.pattern.matcher(requestUri);
                boolean matches = matcher.matches();
                if (matches) {
                    return true;
                }
            }
        }
        return false;
    }


}
