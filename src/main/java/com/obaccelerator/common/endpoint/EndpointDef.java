package com.obaccelerator.common.endpoint;

import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Getter
public enum EndpointDef {

    POST_TOKEN(RequestMethod.POST.name(), "/token", EndpointAccessType.OPEN),
    POST_CLIENT(RequestMethod.POST.name(), "/client", EndpointAccessType.AUTHENTICATED_ELEVATED),
    POST_USER(RequestMethod.POST.name(), "/user", EndpointAccessType.AUTHENTICATED_CLIENT);

    private final String method;
    private final String path;
    private EndpointAccessType accessType;

    EndpointDef(String method, String path, EndpointAccessType accessType) {
        this.method = method;
        this.path = path;
        this.accessType = accessType;
    }

    public static EndpointAccessType getAccessType(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        for (EndpointDef openEndpoint : EndpointDef.values()) {
            if (requestUri.contains(openEndpoint.getPath()) && method.equals(openEndpoint.getMethod())) {
                return openEndpoint.getAccessType();
            }
        }
        throw new RuntimeException("Could not resolve endpoint in EndpointDef");
    }
}
