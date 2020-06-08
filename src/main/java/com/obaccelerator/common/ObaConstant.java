package com.obaccelerator.common;

public class ObaConstant {

    public static final String ROLE_CLAIM = "role";
    public static final String APPLICATION_ID_CLAIM = "application-id";
    public static final String ORGANIZATION_ID_CLAIM = "organization-id";

    /**
     * Using the ROLE_ prefix here to satisfy Spring's RoleVoter. This voter appears in the DecisionManager of
     * MethodSecurityInterceptor when enabling @Secured annotations.
     */
    public static final String APPLICATION = "ROLE_APPLICATION";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String ORGANIZATION = "ROLE_ORGANIZATION";
    public static final String ANONYMOUS = "ROLE_OBA_ANONYMOUS";


}
