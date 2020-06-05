package com.obaccelerator.common.token;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.lang.JoseException;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TokenReader {

    private TokenReader() {
    }

    private static final String TOKEN_EXCEPTION = "Token processing exception";

    private static final JwtConsumer UNSAFE_JWT_CONSUMER = new JwtConsumerBuilder()
            .setSkipSignatureVerification()
            .setSkipDefaultAudienceValidation()
            .build();


    public static Optional<String> getTokenFromHeader(HttpServletRequest request) {
        return getTokenFromHeader(request.getHeader("Authorization"));
    }

    public static Optional<String> getTokenFromHeader(WebRequest request) {
        return getTokenFromHeader(request.getHeader("Authorization"));
    }

    private static Optional<String> getTokenFromHeader(String authorization) {
        if (authorization == null || authorization.length() == 0) {
            return Optional.empty();
        }

        return Optional.of(authorization.replace("Bearer", "").trim());
    }


    public static Map<String, Object> readClaims(String token, PublicKey publicKey) throws ApiTokenProcessingException {
        JwtConsumer consumer = new JwtConsumerBuilder().setVerificationKey(publicKey)
                .build();

        JwtClaims jwtClaims = null;
        try {
            jwtClaims = consumer.processToClaims(token);
        } catch (InvalidJwtException e) {
            processingException(e);
        }

        Map<String, Object> claims = new HashMap<>();
        jwtClaims.getClaimsMap().forEach(claims::put);
        return claims;
    }

    public static Map<String, Object> readClaims(String token, String audience, PublicKey publicKey) throws ApiTokenProcessingException {
        JwtConsumer consumer = new JwtConsumerBuilder().setVerificationKey(publicKey).setExpectedAudience(audience)
                .build();

        JwtClaims jwtClaims = null;
        try {
            jwtClaims = consumer.processToClaims(token);
        } catch (InvalidJwtException e) {
            processingException(e);
        }

        Map<String, Object> claims = new HashMap<>();
        jwtClaims.getClaimsMap().forEach(claims::put);
        return claims;
    }

    public static Optional<String> readClaim(String token, String claim, PublicKey publicKey) throws ApiTokenProcessingException {
        return Optional.ofNullable((String) readClaims(token, publicKey).get(claim));
    }


    public static Optional<String> readClaim(String token, String claim, String publicKey) throws ApiTokenProcessingException {
        return Optional.ofNullable((String) readClaims(token, KeyUtil.stringToRsaPublicKey(publicKey)).get(claim));
    }

    public static Optional<String> readHeader(String token, String header, PublicKey publicKey) throws ApiTokenProcessingException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setVerificationKey(publicKey)
                .build();
        return findStringHeader(token, header, jwtConsumer);
    }

    public static Optional<String> readHeaderWithoutSignatureVerification(final String token, final String header) throws ApiTokenProcessingException {
        return findStringHeader(token, header, UNSAFE_JWT_CONSUMER);
    }

    public static Optional<String> readClaimWithoutSignatureVerification(final String token, final String claim) throws ApiTokenProcessingException {
        try {
            return Optional.ofNullable((String) UNSAFE_JWT_CONSUMER.processToClaims(token).getClaimsMap().get(claim));
        } catch (InvalidJwtException e) {
            processingException(e);
        }

        return Optional.empty();
    }

    public static Map<String, Object> readClaimsWithoutSignatureVerification(final String token) throws ApiTokenProcessingException {
        try {
            return UNSAFE_JWT_CONSUMER.processToClaims(token).getClaimsMap().entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        } catch (InvalidJwtException e) {
            processingException(e);
        }

        return null;
    }

    public static void verifySignature(final String token, final PublicKey publicKey) throws ApiTokenProcessingException, ApiTokenSignatureValidationException {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA256));
        try {
            jws.setCompactSerialization(token);
            jws.setKey(publicKey);
            boolean b = jws.verifySignature();
            if (!b) {
                throw new ApiTokenSignatureValidationException();
            }
        } catch (JoseException e) {
            processingException(e);
        }
    }

    public static void verifySignature(final String token, final String publicKey) throws ApiTokenSignatureValidationException, ApiTokenProcessingException {
        verifySignature(token, KeyUtil.stringToRsaPublicKey(publicKey));
    }

    private static Optional<String> findStringHeader(String token, String header, JwtConsumer jwtConsumer) throws ApiTokenProcessingException {
        JwtContext context = null;
        try {
            context = jwtConsumer.process(token);
        } catch (InvalidJwtException e) {
            processingException(e);
        }

        for (JsonWebStructure jsonWebStructure : context.getJoseObjects()) {
            String headerObject = jsonWebStructure.getHeaders().getStringHeaderValue(header);
            return Optional.ofNullable(headerObject);
        }
        return Optional.empty();
    }

    private static void processingException(Exception e) throws ApiTokenProcessingException {
        throw new ApiTokenProcessingException(TOKEN_EXCEPTION + " : " + e.getMessage(), e);
    }


}
