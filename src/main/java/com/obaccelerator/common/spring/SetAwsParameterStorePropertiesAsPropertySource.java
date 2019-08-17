package com.obaccelerator.common.spring;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Collects all properties present in AWS Parameter Store and checks their keys against a map provided by the application
 * in the abstract method. The matching properties are inserted into the Spring environment with the key coming
 * from the map and the value from AWS Parameter Store.
 *
 * This strategy allows an application to set for example predefined Spring properties with values from AWS
 * Parameter Store.
 */
@Slf4j
public abstract class SetAwsParameterStorePropertiesAsPropertySource implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static AWSSimpleSystemsManagement AWS_CLIENT;

    static {
        AWSSimpleSystemsManagementClientBuilder clientBuilder = AWSSimpleSystemsManagementClientBuilder.standard();
        clientBuilder.setRegion("eu-central-1");
        AWS_CLIENT = clientBuilder.build();
    }

    public abstract Map<String, List<String>> getAwsParametersToNeededProperties();

    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {

        DescribeParametersResult describeParametersResult = AWS_CLIENT.describeParameters(new DescribeParametersRequest());

        Map<String, List<String>> awsParametersToNeededProperties = getAwsParametersToNeededProperties();
        Properties secureProps = new Properties();
        if(awsParametersToNeededProperties != null) {
            for (Map.Entry<String, List<String>> propertyEntry : awsParametersToNeededProperties.entrySet()) {
                String awsProperty = propertyEntry.getKey();
                List<String> setAsProperties = propertyEntry.getValue();
                String value = fetchAwsPropertyValue(awsProperty);
                for (String setAsProperty : setAsProperties) {
                    secureProps.setProperty(setAsProperty, value);
                }
            }
        }
        event.getEnvironment().getPropertySources().addFirst(new PropertiesPropertySource("myProps", secureProps));
    }

    public String fetchAwsPropertyValue(String awsProperty) {
        GetParameterRequest getParameterRequest = new GetParameterRequest();
        getParameterRequest.setName(awsProperty);
        getParameterRequest.setWithDecryption(true);
        try {
            GetParameterResult parameterResult = AWS_CLIENT.getParameter(getParameterRequest);
            return parameterResult.getParameter().getValue();
        } catch(ParameterNotFoundException e) {
            throw new RuntimeException("Could not find parameter " + awsProperty + " in AWS", e);
        }


    }
}
