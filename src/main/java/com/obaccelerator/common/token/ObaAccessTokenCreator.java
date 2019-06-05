package com.obaccelerator.common.token;

import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.JwtClaims;

import java.security.KeyStore;
import java.util.UUID;


@Slf4j
public class ObaAccessTokenCreator extends TokenCreator {

    private static final int TOKEN_VALIDITY_PERIOD_MS = 30 * 60 * 1000;


    public static ObaAccessTokenCreator forPfx(String pfxPath, String pfxPassword, String privateKeyAlias) {
        ObaAccessTokenCreator util = new ObaAccessTokenCreator();
        KeyStore keyStore = PfxUtil.loadKeyStore(pfxPath, pfxPassword);
        PfxUtil pfxUtil = new PfxUtil(pfxPath, pfxPassword);
        util.privateKey = pfxUtil.getPrivateKey(privateKeyAlias);
        return util;
    }

    /**
     * An OBA token is valid for 30 minutes.
     * @param clientId
     * @return
     */
    public String createObaAccessToken(String clientId) {
        final long millis = System.currentTimeMillis();
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("client_id", clientId);
        jwtClaims.setClaim("iat", millis);
        jwtClaims.setClaim("exp", millis + TOKEN_VALIDITY_PERIOD_MS);
        jwtClaims.setClaim("jti", UUID.randomUUID());
        return createTokenFromClaims(jwtClaims);
    }


}
