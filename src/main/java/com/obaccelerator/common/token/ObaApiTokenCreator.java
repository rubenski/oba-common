package com.obaccelerator.common.token;

import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.JwtClaims;

import java.util.UUID;


@Slf4j
public class ObaApiTokenCreator extends TokenCreator {

    public static ObaApiTokenCreator forPfx(String pfxPath, String pfxPassword, String privateKeyAlias) {
        ObaApiTokenCreator util = new ObaApiTokenCreator();
        util.privateKey = new PfxUtil(pfxPath, pfxPassword).getPrivateKey(privateKeyAlias);
        return util;
    }

    /**
     * An OBA token is valid for 30 minutes.
     *
     * @param clientId
     * @return
     */
    public String createObaAccessToken(String clientId, int validityMinutes) {
        final long millis = System.currentTimeMillis();
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("client_id", clientId);
        jwtClaims.setClaim("iat", millis);
        jwtClaims.setClaim("exp", millis + validityMinutes * 60 * 1000);
        jwtClaims.setClaim("jti", UUID.randomUUID());
        return createTokenFromClaims(jwtClaims);
    }


}
