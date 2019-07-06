package com.obaccelerator.common.spring;


import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

/**
 * Adds the ParameterStorePropertySource to the Spring Environment
 */
public class ParameterStorePropertySourceEnvironmentPostProcessor implements EnvironmentPostProcessor {

    public ParameterStorePropertySourceEnvironmentPostProcessor() {
        String test = "";
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        AWSSimpleSystemsManagementClientBuilder clientBuilder = AWSSimpleSystemsManagementClientBuilder.standard();
        clientBuilder.setRegion("eu-central-1");
        ParameterStorePropertySource parameterStorePropertySource =
                new ParameterStorePropertySource("AWSParameterStorePropertySource", clientBuilder.build());
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addLast(parameterStorePropertySource);
    }
}
