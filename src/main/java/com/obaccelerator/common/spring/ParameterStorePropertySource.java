package com.obaccelerator.common.spring;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertySource;

@Slf4j
public class ParameterStorePropertySource extends PropertySource<AWSSimpleSystemsManagement> {
    public ParameterStorePropertySource(String name, AWSSimpleSystemsManagement source) {
        super(name, source);
    }

    @Override
    public Object getProperty(String propertyName) {
        try {
            if (propertyName.startsWith("ext_")) {
                String value = source.getParameter(new GetParameterRequest().withName(propertyName)
                        .withWithDecryption(true))
                        .getParameter()
                        .getValue();
                log.info("Found a property in AWS Parameter Store: " + propertyName );
                return value;
            }
        } catch (ParameterNotFoundException e) {
            return null;
        }

        return null;
    }
}
