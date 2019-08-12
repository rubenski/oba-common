package com.obaccelerator.common.spring;


import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

/**
 * Adds the ParameterStorePropertySource to the Spring Environment.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ParameterStorePropertySourceEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        AWSSimpleSystemsManagementClientBuilder clientBuilder = AWSSimpleSystemsManagementClientBuilder.standard();
        clientBuilder.setRegion("eu-central-1");
        clientBuilder.build();
        ParameterStorePropertySource parameterStorePropertySource =
                new ParameterStorePropertySource("AWSParameterStorePropertySource", clientBuilder.build());
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addLast(parameterStorePropertySource);
    }
}
