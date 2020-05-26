package com.obaccelerator.common.endpoint;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpenEndpointTest {

    private HttpServletRequest request;

    @Test
    void getAuthenticatedEndpoint() {
        String endpoint = "/user/5a642120-7f2d-4a01-af76-45f9dd86475e";
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn("GET");
        assertFalse(OpenEndpoint.isOpenEndpoint(request));
    }

    @Test
    void getOpenEndpoint() {
        String endpoint = "/tokens";
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn("GET");
        assertTrue(OpenEndpoint.isOpenEndpoint(request));
    }
}
