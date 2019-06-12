package com.obaccelerator.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Turns validation errors into error messages that follow the common OBA error message structure defined by
 * ObaErrorMessage. The reason this is done in a custom error controller is that extending Spring's
 * ResponseEntityExceptionHandler and using that with @ControllerAdvice doesn't work for handling validation errors.
 * The messages it generates are empty, so you get only a status code instead of a pretty message saying which fields
 * contained errors.
 */
@Slf4j
@RequestMapping("${server.error.path:${error.path:/error}}")
@Controller
public class ObaErrorController extends AbstractErrorController {


    public ObaErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping
    public ResponseEntity<ObaErrorMessage> error(HttpServletRequest request) {
        RequestAttributes attributes = new ServletWebRequest(request);
        Throwable error = (Throwable) attributes.getAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR", 0);
        HttpStatus status = getStatus(request);


        // Collect binding errors
        Map<String, String> bindingErrors = collectBindingErrors(error);
        ObaErrorMessage obaErrorMessage;

        // If the errors are Spring binding errors...
        if (bindingErrors != null) {
            obaErrorMessage = new ObaErrorMessage(status.value(), null, "Your message contained errors");
            obaErrorMessage.addFieldErrors(bindingErrors);
            return new ResponseEntity<>(obaErrorMessage, status);
        } else {
            String servletErrorMessage = (String) request.getAttribute("javax.servlet.error.message");
            if (servletErrorMessage != null && servletErrorMessage.trim().length() > 0) {
                ObaErrorMessage errorMessage = new ObaErrorMessage(status.value(), null, servletErrorMessage);
                return new ResponseEntity<>(errorMessage, status);
            }
        }

        throw new RuntimeException("Errors should always be handled. Either in ObaExceptionHandler or here in ObaErrorController. Fix this.");
    }

    private Map<String, String> collectBindingErrors(Throwable error) {
        Map<String, String> bindingErrors;
        if (error instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) error).getBindingResult();
            bindingErrors = new HashMap<>();
            bindingResult.getAllErrors().forEach(e -> {
                String objectName = e.getObjectName();
                String defaultMessage = e.getDefaultMessage();
                bindingErrors.put(objectName, defaultMessage);
            });
            return bindingErrors;
        }

        return null;
    }
}
