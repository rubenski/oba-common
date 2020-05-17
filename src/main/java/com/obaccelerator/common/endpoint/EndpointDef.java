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

    GET_TOKENS(RequestMethod.GET.name(), Path.GET_TOKENS, EndpointAccessType.OPEN),
    GET_ELEVATED_TOKENS(RequestMethod.GET.name(), Path.GET_ELEVATED_TOKENS, EndpointAccessType.OPEN),
    POST_APPLICATION(RequestMethod.POST.name(), Path.POST_APPLICATION, EndpointAccessType.PORTAL_CLIENT),
    POST_USER(RequestMethod.POST.name(), Path.POST_USERS, EndpointAccessType.API_CLIENT),
    GET_USER(RequestMethod.GET.name(), Path.GET_USERS, EndpointAccessType.API_CLIENT),
    DELETE_APPLICATIONS(RequestMethod.DELETE.name(), Path.DELETE_APPLICATIONS, EndpointAccessType.PORTAL_CLIENT),
    POST_APPLICATION_KEYS(RequestMethod.POST.name(), Path.POST_APPLICATION_KEYS, EndpointAccessType.PORTAL_CLIENT),
    GET_APPLICATION_KEYS(RequestMethod.GET.name(), Path.GET_APPLICATION_KEYS, EndpointAccessType.API_CLIENT),
    POST_ORGANIZATIONS(RequestMethod.POST.name(), Path.POST_ORGANIZATIONS, EndpointAccessType.PORTAL_CLIENT),
    PUT_ORGANIZATIONS(RequestMethod.PUT.name(), Path.PUT_ORGANIZATIONS, EndpointAccessType.PORTAL_CLIENT),
    GET_ORGANIZATIONS(RequestMethod.GET.name(), Path.GET_ORGANIZATIONS, EndpointAccessType.PORTAL_CLIENT),
    GET_ORGANIZATION(RequestMethod.GET.name(), Path.GET_ORGANIZATION, EndpointAccessType.PORTAL_CLIENT),
    POST_SESSIONS(RequestMethod.POST.name(), Path.POST_SESSIONS, EndpointAccessType.PORTAL_CLIENT),
    GET_TRANSACTIONS(RequestMethod.GET.name(), Path.GET_TRANSACTIONS, EndpointAccessType.API_CLIENT),
    POST_CERTIFICATES(RequestMethod.POST.name(), Path.POST_CERTIFICATES, EndpointAccessType.PORTAL_CLIENT),
    GET_CERTIFICATES(RequestMethod.GET.name(), Path.GET_CERTIFICATES, EndpointAccessType.PORTAL_CLIENT);

    private static final Map<String, Pattern> CACHE = new HashMap<>();
    private final String method;
    private final String path;
    private final EndpointAccessType accessType;

    EndpointDef(String method, String path, EndpointAccessType accessType) {
        this.method = method;
        this.path = path;
        this.accessType = accessType;
    }

    public static class Path {
        public static final String GET_USERS = "/users/{userId}";
        public static final String POST_USERS = "/users";
        public static final String POST_APPLICATION = "/applications";
        public static final String POST_APPLICATION_KEYS = "/applications/{applicationId}/keys";
        public static final String GET_APPLICATION_KEYS = "/applications/{applicationId}/keys";
        public static final String DELETE_APPLICATIONS = "/applications/{applicationId}";
        public static final String GET_TOKENS = "/tokens";
        public static final String GET_ELEVATED_TOKENS = "/elevated-tokens";
        public static final String POST_ORGANIZATIONS = "/organizations";
        public static final String PUT_ORGANIZATIONS = "/organizations";
        public static final String GET_ORGANIZATIONS = "/organizations";
        public static final String GET_ORGANIZATION = "/organizations/{organizationId}";
        public static final String POST_SESSIONS = "/sessions";
        public static final String GET_TRANSACTIONS = "/transactions";
        public static final String POST_CERTIFICATES = "/{organizationId}/certificates";
        public static final String GET_CERTIFICATES = "/{organizationId}/certificates";
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
        throw new EndpointUndefinedException(method + " " + requestUri + " not found");
    }

    public boolean isOpenEndpoint() {
        return accessType.equals(EndpointAccessType.OPEN);
    }

    public boolean isPortalClientEndpoint() {
        return accessType.equals(EndpointAccessType.PORTAL_CLIENT);
    }

    public boolean isApiClientEndpoint() {
        return accessType.equals(EndpointAccessType.API_CLIENT);
    }

}
