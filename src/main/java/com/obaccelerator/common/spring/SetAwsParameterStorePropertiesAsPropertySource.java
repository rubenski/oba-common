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
import java.util.Optional;
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

    public SetAwsParameterStorePropertiesAsPropertySource() {
    }

    public abstract Map<String, List<String>> getAwsParametersToNeededProperties();


    public Optional<List<String>> setAwsParameterAs(String awsParameterName) {
        for (String awsPropertyName : getAwsParametersToNeededProperties().keySet()) {
            if(awsParameterName.equals(awsPropertyName)) {
                return Optional.of(getAwsParametersToNeededProperties().get(awsParameterName));
            }
        }
        return Optional.empty();
    }

    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {

        AWSSimpleSystemsManagementClientBuilder clientBuilder = AWSSimpleSystemsManagementClientBuilder.standard();
        clientBuilder.setRegion("eu-central-1");
        AWSSimpleSystemsManagement awsParameterStoreClient = clientBuilder.build();
        ParameterStorePropertySource parameterStorePropertySource =
                new ParameterStorePropertySource("AWSParameterStorePropertySource", awsParameterStoreClient);

        DescribeParametersRequest parametersRequest = new DescribeParametersRequest();
        DescribeParametersResult describeParametersResult = awsParameterStoreClient.describeParameters(parametersRequest);

        Properties secureProps = new Properties();
        for (ParameterMetadata parameter : describeParametersResult.getParameters()){
            Optional<List<String>> stringListOptional = setAwsParameterAs(parameter.getName());
            if(stringListOptional.isPresent()) {
                log.info("Fetching parameter named {} from AWS for insertion into environment", parameter.getName());
                GetParameterRequest getParameterRequest = new GetParameterRequest();
                getParameterRequest.setName(parameter.getName());
                getParameterRequest.setWithDecryption(true);
                GetParameterResult decryptedParameter = awsParameterStoreClient.getParameter(getParameterRequest);
                for (String newProperty : stringListOptional.get()) {
                    secureProps.setProperty(newProperty, decryptedParameter.getParameter().getValue());
                }
            }
        }

        event.getEnvironment().getPropertySources().addFirst(new PropertiesPropertySource("myProps", secureProps));
    }
}
