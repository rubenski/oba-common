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

    public static EndpointDef getEndpoint(HttpServletRequest request) throws EndpointUndefinedException {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        for (EndpointDef endpoint : EndpointDef.values()) {
            if (requestUri.contains(endpoint.getPath()) && method.equals(endpoint.getMethod())) {
                return endpoint;
            }
        }
        throw new EndpointUndefinedException("Endpoint " + requestUri + " is undefined");
    }

    public boolean isOpen() {
        return accessType.equals(EndpointAccessType.OPEN);
    }

    public boolean isAuthenticatedElevated() {
        return accessType.equals(EndpointAccessType.AUTHENTICATED_ELEVATED);
    }

    public boolean isAuthenticatedClient() {
        return accessType.equals(EndpointAccessType.AUTHENTICATED_CLIENT);
    }
}
