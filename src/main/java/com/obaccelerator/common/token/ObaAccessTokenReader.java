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

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ObaAccessTokenReader {


    public Map<String, Object> readClaims(String token, PublicKey publicKey) throws InvalidJwtException {
        JwtConsumer consumer = new JwtConsumerBuilder().setVerificationKey(publicKey)
                .build();
        JwtClaims jwtClaims = consumer.processToClaims(token);
        Map<String, Object> claims = new HashMap<>();
        jwtClaims.getClaimsMap().forEach(claims::put);
        return claims;
    }

    public Optional<String> readClaim(String token, String claim, PublicKey publicKey) throws InvalidJwtException {
        return Optional.ofNullable((String) readClaims(token, publicKey).get(claim));
    }


    public Optional<String> readClaim(String token, String claim, String publicKey) throws InvalidJwtException {
        return Optional.ofNullable((String) readClaims(token, stringToPublicKey(publicKey)).get(claim));
    }

    public Optional<String> readHeader(String token, String header, PublicKey publicKey) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setVerificationKey(publicKey)
                .build();
        return findStringHeader(token, header, jwtConsumer);
    }

    public Optional<String> readHeaderWithoutSignatureVerification(final String token, final String header) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setSkipSignatureVerification()
                .build();
        return findStringHeader(token, header, jwtConsumer);
    }

    public boolean verifySignature(final String token, final String publicKey) throws ObaAccessTokenException {
        return verifySignature(token, stringToPublicKey(publicKey));
    }

    public boolean verifySignature(final String token, final PublicKey publicKey) throws ObaAccessTokenException {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA512));
        try {
            jws.setCompactSerialization(token);
            jws.setKey(publicKey);
            return jws.verifySignature();
        } catch (JoseException e) {
            throw new ObaAccessTokenException("Exception while verifying token. This doesn't happen because the signature was not valid", e);
        }
    }

    private Optional<String> findStringHeader(String token, String header, JwtConsumer jwtConsumer) throws InvalidJwtException {
        JwtContext context = jwtConsumer.process(token);

        for (JsonWebStructure jsonWebStructure : context.getJoseObjects()) {
            String headerObject = jsonWebStructure.getHeaders().getStringHeaderValue(header);
            if (headerObject != null) {
                return Optional.ofNullable(headerObject);
            }
        }
        return Optional.empty();
    }

    private static PublicKey stringToPublicKey(String keyString) {
        //byte[] publicBytes = Base64.getDecoder().decode(keyString);
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


}
