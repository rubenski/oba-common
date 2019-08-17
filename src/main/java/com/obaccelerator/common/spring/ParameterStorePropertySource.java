package com.obaccelerator.common.spring;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertySource;

/**
 * Fetches properties starting with ext_ from AWS ParameterStore. Authentication is performed using
 * either environment variables or AWS instance role (or other options in the authentication chain)
 */
@Slf4j
public class ParameterStorePropertySource extends PropertySource<AWSSimpleSystemsManagement> {

    public ParameterStorePropertySource(String name, AWSSimpleSystemsManagement source) {
        super(name, source);
    }

    @Override
    public Object getProperty(String propertyName) {
        try {
            // ext_ prefix prevents a call to AWS for each and every property that was not resolved by previous property sources
            if (propertyName.startsWith("ext_")) {
                log.info("Fetching parameter {} from AWS Parameter Store", propertyName);
                return source.getParameter(new GetParameterRequest().withName(propertyName)
                        .withWithDecryption(true))
                        .getParameter()
                        .getValue();
            }
        } catch (ParameterNotFoundException e) {
            throw new RuntimeException("Could not find AWS parameter " + propertyName, e);
        }
        return null;
    }
}
