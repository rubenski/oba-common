package com.obaccelerator.common.spring;

import com.obaccelerator.common.error.ObaError;
import com.obaccelerator.common.error.ObaException;
import com.obaccelerator.common.token.TokenReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.security.PublicKey;
import java.util.Optional;

import static com.obaccelerator.common.ObaConstant.INTERNAL_TOKEN_HEADER;

/**
 * Argument resolver that takes the incoming internal-token JWT token, extracts the client_id claim and returns that
 * as a ObaRequestContext object. The ObaRequestContext object may grow woth new client-specific fields such as
 * per-client configuration in the future. This approach prevents us from having to extract the client id manually
 * in each and every controller method where the client id is needed (which is basically almost every controller method)
 */
@Slf4j
public class ObaRequestContextMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private PublicKey publicKey;

    public ObaRequestContextMethodArgumentResolver(final PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return findMethodAnnotation(RequestContext.class, parameter) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String internalToken = webRequest.getHeader(INTERNAL_TOKEN_HEADER);
        TokenReader reader = new TokenReader();
        Optional<String> optionalClientId = reader.readClaim(internalToken, "client_id", publicKey);
        if (!optionalClientId.isPresent()) {
            throw new ObaException(ObaError.OBA_INVALID_INTERNAL_TOKEN);
        }

        return new ObaRequestContext(optionalClientId.get());
    }

    private <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass,
                                                          MethodParameter parameter) {
        T annotation = parameter.getParameterAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }
        Annotation[] annotationsToSearch = parameter.getParameterAnnotations();
        for (Annotation toSearch : annotationsToSearch) {
            annotation = AnnotationUtils.findAnnotation(toSearch.annotationType(),
                    annotationClass);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }
}
