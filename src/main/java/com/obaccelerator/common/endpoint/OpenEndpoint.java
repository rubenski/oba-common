package com.obaccelerator.common.endpoint;

import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public enum OpenEndpoint {

    GET_TOKENS(RequestMethod.GET.name(), Path.GET_TOKENS);

    private static final Map<String, Pattern> CACHE = new HashMap<>();
    private final String method;
    private final String path;

    OpenEndpoint(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public static class Path {
        public static final String GET_TOKENS = "/tokens";
    }

    public static Optional<OpenEndpoint> getEndpoint(HttpServletRequest request) throws EndpointUndefinedException {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        for (OpenEndpoint endpoint : OpenEndpoint.values()) {
            if (method.equals(endpoint.getMethod())) {
                String path = endpoint.getPath();
                String urlPattern = path.replaceAll("\\{[0-9a-zA-Z-]+}", "[0-9A-Za-z-]+?");
                Pattern regexPattern = CACHE.get(urlPattern);
                if (regexPattern == null) {
                    regexPattern = Pattern.compile(urlPattern);
                }
                CACHE.put(urlPattern, regexPattern);
                Matcher matcher = regexPattern.matcher(requestUri);
                boolean matches = matcher.matches();
                if (matches) {
                    return Optional.of(endpoint);
                }
            }
        }
        return Optional.empty();
    }


}
