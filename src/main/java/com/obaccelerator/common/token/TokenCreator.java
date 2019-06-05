package com.obaccelerator.common.token;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import java.security.Key;
import java.util.Map;

abstract class TokenCreator {

    protected Key privateKey;


    protected String createTokenFromClaims(JwtClaims jwtClaims, Map<String, String> headers) {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        jws.setKey(privateKey);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);

        for (String s : headers.keySet()) {
            jws.setHeader(s, headers.get(s));
        }

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }

    protected String createTokenFromClaims(JwtClaims jwtClaims) {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        jws.setKey(privateKey);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }

}
