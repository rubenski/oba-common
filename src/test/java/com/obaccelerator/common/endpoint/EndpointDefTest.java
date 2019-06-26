package com.obaccelerator.common.endpoint;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EndpointDefTest {


    private HttpServletRequest request;


    @Test
    void getEndpoint() throws EndpointUndefinedException {
        String endpoint = "/user/5a642120-7f2d-4a01-af76-45f9dd86475e";
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn("GET");
        EndpointDef endpointDef = EndpointDef.getEndpoint(request);
        assertNotNull(endpointDef);
        assertEquals("/user/{userId}", endpointDef.getPath());
    }




}
