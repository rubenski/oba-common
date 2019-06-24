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

import javax.servlet.http.HttpServletRequest;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
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
            .build();


    public static Optional<String> getTokenFromHeader(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");

        if(authorization == null || authorization.length() == 0) {
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

    public static Optional<String> readClaim(String token, String claim, PublicKey publicKey) throws ApiTokenProcessingException {
        return Optional.ofNullable((String) readClaims(token, publicKey).get(claim));
    }


    public static Optional<String> readClaim(String token, String claim, String publicKey) throws ApiTokenProcessingException {
        return Optional.ofNullable((String) readClaims(token, stringToPublicKey(publicKey)).get(claim));
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

    public static Map<String, String> readClaimsWithoutSignatureVerification(final String token) throws ApiTokenProcessingException {
        try {
            return UNSAFE_JWT_CONSUMER.processToClaims(token).getClaimsMap().entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey(), e -> (String) e.getValue()));
        } catch (InvalidJwtException e) {
            processingException(e);
        }

        return null;
    }

    public static void verifySignature(final String token, final PublicKey publicKey) throws ApiTokenProcessingException, ApiTokenSignatureValidationException {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA512));
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
        verifySignature(token, stringToPublicKey(publicKey));
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

    private static PublicKey stringToPublicKey(String keyString) {
        String cleaned = keyString.replaceAll("\\n", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(cleaned.getBytes()));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processingException(Exception e) throws ApiTokenProcessingException {
        throw new ApiTokenProcessingException(TOKEN_EXCEPTION, e);
    }


}
