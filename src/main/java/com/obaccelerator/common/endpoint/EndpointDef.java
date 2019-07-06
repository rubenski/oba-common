package com.obaccelerator.common.endpoint;

import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public enum EndpointDef {

    GET_TOKEN(RequestMethod.GET.name(), Path.GET_TOKENS, EndpointAccessType.OPEN),
    GET_ELEVATED_TOKEN(RequestMethod.GET.name(), Path.GET_ELEVATED_TOKENS, EndpointAccessType.OPEN),
    POST_CLIENT(RequestMethod.POST.name(), Path.POST_CLIENTS, EndpointAccessType.ELEVATED),
    POST_USER(RequestMethod.POST.name(), Path.POST_USERS, EndpointAccessType.CLIENT_AUTHENTICATED),
    GET_USER(RequestMethod.GET.name(), Path.GET_USERS, EndpointAccessType.CLIENT_AUTHENTICATED),
    DELETE_CLIENTS(RequestMethod.DELETE.name(), Path.DELETE_CLIENTS, EndpointAccessType.CLIENT_ADMIN),
    POST_CLIENT_KEY(RequestMethod.POST.name(), Path.POST_CLIENTS_KEYS, EndpointAccessType.ELEVATED),
    GET_CLIENT_KEYS(RequestMethod.GET.name(), Path.GET_CLIENTS_KEYS, EndpointAccessType.CLIENT_AUTHENTICATED);

    private static final Map<String, Pattern> CACHE = new HashMap<>();
    private final String method;
    private final String path;
    private EndpointAccessType accessType;

    EndpointDef(String method, String path, EndpointAccessType accessType) {
        this.method = method;
        this.path = path;
        this.accessType = accessType;
    }

    public static class Path {
        public static final String GET_USERS = "/users/{userId}";
        public static final String POST_USERS = "/users";
        public static final String POST_CLIENTS = "/clients";
        public static final String POST_CLIENTS_KEYS = "/clients/{clientId}/keys";
        public static final String GET_CLIENTS_KEYS = "/clients/{clientId}/keys";
        public static final String DELETE_CLIENTS = "/clients/{clientId}";
        public static final String GET_TOKENS = "/tokens";
        public static final String GET_ELEVATED_TOKENS = "/elevated-tokens";
    }

    public static EndpointDef getEndpoint(HttpServletRequest request) throws EndpointUndefinedException {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        for (EndpointDef endpoint : EndpointDef.values()) {
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
                    return endpoint;
                }
            }
        }
        throw new EndpointUndefinedException("Endpoint " + method + " " + requestUri + " is undefined");
    }

    public boolean isOpen() {
        return accessType.equals(EndpointAccessType.OPEN);
    }

    public boolean isAuthenticatedElevated() {
        return accessType.equals(EndpointAccessType.ELEVATED);
    }

    public boolean isAuthenticatedClient() {
        return accessType.equals(EndpointAccessType.CLIENT_AUTHENTICATED);
    }

}
