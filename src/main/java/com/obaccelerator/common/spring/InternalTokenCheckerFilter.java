package com.obaccelerator.common.spring;

import com.obaccelerator.common.endpoint.OpenEndpoint;
import com.obaccelerator.common.error.ObaError;
import com.obaccelerator.common.error.ObaException;
import com.obaccelerator.common.token.ApiTokenException;
import com.obaccelerator.common.token.TokenReader;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PublicKey;

import static com.obaccelerator.common.ObaConstant.INTERNAL_TOKEN_HEADER;

@Component
public class InternalTokenCheckerFilter extends OncePerRequestFilter {

    private final PublicKey internalTokenPublicKey;

    public InternalTokenCheckerFilter(PublicKey internalTokenPublicKey) {
        this.internalTokenPublicKey = internalTokenPublicKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(INTERNAL_TOKEN_HEADER);
        if (token == null) {
            throw new ObaException(ObaError.OBA_MISSING_INTERNAL_TOKEN_HEADER);
        }

        TokenReader reader = new TokenReader();
        try {
            reader.verifySignature(token, internalTokenPublicKey);
        } catch (ApiTokenException e) {
            throw new ServletException(e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return OpenEndpoint.isOpenEndpoint(request);
    }
}
