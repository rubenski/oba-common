package com.obaccelerator.common.endpoint;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpenEndpointTest {

    private HttpServletRequest request;

    @Test
    void getAuthenticatedEndpoint() throws EndpointUndefinedException {
        String endpoint = "/user/5a642120-7f2d-4a01-af76-45f9dd86475e";
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn("GET");
        Optional<OpenEndpoint> openEndpoint = OpenEndpoint.getEndpoint(request);
        assertTrue(openEndpoint.isEmpty());
    }

    @Test
    void getOpenEndpoint() throws EndpointUndefinedException {
        String endpoint = "/tokens";
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn("GET");
        Optional<OpenEndpoint> openEndpoint = OpenEndpoint.getEndpoint(request);
        assertTrue(openEndpoint.isPresent());
        assertEquals("/tokens", openEndpoint.get().getPath());
    }
}
