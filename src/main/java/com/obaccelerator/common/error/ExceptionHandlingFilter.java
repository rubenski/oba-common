package com.obaccelerator.common.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.obaccelerator.common.error.ObaError.OBA_FILTER_ERROR;

/**
 * Exceptions occurring in filters will not be handled by @ControllerAdvice exception handlers, because it is too early
 * for that. When exceptions occur in a filter we must catch them always in order to prevent leaking sensitive
 * info or in order to prevent the default Spring /error page from being returned to a client.
 *
 * Therefore we run all application filters inside the ExceptionHandlingFilter, which catches any Throwable and
 * returns an ObaErrorMessage.
 */
@Slf4j
public class ExceptionHandlingFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Throwable e) {
            ObaErrorMessage errorMessage = new ObaErrorMessage(OBA_FILTER_ERROR);
            log.error("Error in filter chain", e);
            response.setStatus(500);
            response.setContentType("application/json");
            response.getWriter().write(convertObjectToJson(errorMessage));
        }
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}