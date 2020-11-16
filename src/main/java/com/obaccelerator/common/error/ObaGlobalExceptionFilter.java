package com.obaccelerator.common.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.obaccelerator.common.error.ObaError.SPRING_SECURITY_FILTER_CHAIN_EXCEPTION;

/**
 * This filter wraps the entire Spring filter chain and catches any exception thrown
 * before a Spring handler method is reached.
 *
 * One example is the RequestRejectedException thrown by StrictHttpFirewall when you use a request URL containing "//"
 *
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ObaGlobalExceptionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(request, response);
        } catch(Throwable t) {
            ObaErrorMessage errorMessage = new ObaErrorMessage(SPRING_SECURITY_FILTER_CHAIN_EXCEPTION);
            log.error("ObaGlobalExceptionFilter caught an exception", t);
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(500);
            resp.setContentType("application/json");
            resp.getWriter().write(convertObjectToJson(errorMessage));
        }

    }

    @Override
    public void destroy() {

    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}
