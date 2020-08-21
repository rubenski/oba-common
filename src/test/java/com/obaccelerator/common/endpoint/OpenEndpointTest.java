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
    void getTokensIsOpenEndpoint() {
        String endpoint = "/tokens";
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn("GET");
        assertTrue(OpenEndpoint.isOpenEndpoint(request));
    }

    @Test
    void getOrganizationsIsOpenEndpoint() {
        String endpoint = "/organizations";
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn("POST");
        assertTrue(OpenEndpoint.isOpenEndpoint(request));
    }

    @Test
    void getOrganizationsIsNoOpenEndpoint() {
        String endpoint = "/organizations";
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn("GET");
        assertFalse(OpenEndpoint.isOpenEndpoint(request));
    }


    @Test
    void getLogoIsOpenEndpoint() {
        String endpoint = "/logos/rabobank-60.png";
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn("GET");
        assertTrue(OpenEndpoint.isOpenEndpoint(request));
    }

    @Test
    void getLogoWithSlashIsNoOpenEndpoint() {
        String endpoint = "/logos/raboba/nk-60.png";
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn("GET");
        assertFalse(OpenEndpoint.isOpenEndpoint(request));
    }
}
