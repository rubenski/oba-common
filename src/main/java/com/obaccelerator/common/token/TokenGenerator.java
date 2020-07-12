package com.obaccelerator.common.token;

import com.google.protobuf.Api;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import java.security.Key;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.obaccelerator.common.ObaConstant.*;

@Slf4j
public class TokenGenerator {

    private static final long TEMP_TOKEN_VALIDITY_MS = 24 * 60 * 60 * 1000;

    private static final String ALGORITHM = AlgorithmIdentifiers.RSA_USING_SHA256;
    private static final long INTERNAL_TOKEN_VALIDITY_MS = 10 * 1000;
    private static final long API_TOKEN_VALIDITY_MS = 10 * 60 * 1000;

    private Key privateKey;

    public TokenGenerator(String pfxPath, String pfxPassword, String privateKeyAlias) {
        privateKey = new PfxUtil(pfxPath, pfxPassword).getPrivateKey(privateKeyAlias);
    }

    public ApiToken generateApiToken(UUID organizationId, String applicationId, String externalClientRoleName) {
        return generateToken(API_TOKEN_VALIDITY_MS,
                new HashMap<String, String>() {{
                    put(APPLICATION_ID_CLAIM, applicationId);
                    put(ORGANIZATION_ID_CLAIM, organizationId.toString());
                    put(ROLE_CLAIM, externalClientRoleName);
                }},
                Collections.emptyMap());
    }

    public ApiToken generateApplicationToken(String applicationId) {
        return generateToken(INTERNAL_TOKEN_VALIDITY_MS,
                new HashMap<String, String>() {{
                    put(APPLICATION_ID_CLAIM, applicationId);
                    put(ROLE_CLAIM, APPLICATION);
                }},
                Collections.emptyMap());
    }

    public ApiToken generateOrganizationToken(String organizationId) {
        return generateToken(INTERNAL_TOKEN_VALIDITY_MS,
                new HashMap<String, String>() {{
                    put(ORGANIZATION_ID_CLAIM, organizationId);
                    put(ROLE_CLAIM, ORGANIZATION);
                }},
                Collections.emptyMap());
    }

    public ApiToken generateToken(String roleName) {
        return generateToken(API_TOKEN_VALIDITY_MS,
                new HashMap<String, String>() {{
                    put(ROLE_CLAIM, roleName);
                }},
                Collections.emptyMap());
    }

    public ApiToken generateInternalElevatedToken(String internalElevatedRoleName) {
        return generateToken(INTERNAL_TOKEN_VALIDITY_MS,
                new HashMap<String, String>() {{
                    put(ROLE_CLAIM, internalElevatedRoleName);
                }},
                Collections.emptyMap());
    }

    public ApiToken generateToken(long validityMs, Map<String, String> additionalClaims, Map<String, String> headers) {
        final long millis = System.currentTimeMillis();
        final long expires = millis + TEMP_TOKEN_VALIDITY_MS;
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("iat", millis);
        jwtClaims.setClaim("exp", expires); // valid for 10 minutes
        jwtClaims.setClaim("jti", UUID.randomUUID());
        for (String s : additionalClaims.keySet()) {
            jwtClaims.setClaim(s, additionalClaims.get(s));
        }
        String tokenFromClaims = createTokenFromClaims(jwtClaims, headers);
        return new ApiToken(tokenFromClaims, expires);
    }

    private String createTokenFromClaims(JwtClaims jwtClaims, Map<String, String> headers) {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        jws.setKey(privateKey);
        jws.setAlgorithmHeaderValue(ALGORITHM);
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, ALGORITHM));

        for (String s : headers.keySet()) {
            jws.setHeader(s, headers.get(s));
        }

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }
}
