package com.obaccelerator.common.token;

import org.jose4j.jwt.JwtClaims;

import java.util.HashMap;
import java.util.UUID;

public class ObaRequestTokenCreator extends TokenCreator {


    private ObaRequestTokenCreator() {
    }

    /**
     * This doesn't need a PFX, just a private key, but these often come from PFX's
     *
     * @param pfxPath
     * @param pfxPassword
     * @param privateKeyAlias
     * @return
     */
    public static ObaRequestTokenCreator forPfx(String pfxPath, String pfxPassword, String privateKeyAlias) {
        ObaRequestTokenCreator util = new ObaRequestTokenCreator();
        util.privateKey = new PfxUtil(pfxPath, pfxPassword).getPrivateKey(privateKeyAlias);
        return util;
    }

    /**
     * There are some rules for client tokens. They must have an iat (issued at) claim. The issued at time cannot
     * be more than 20 seconds in the past. Also, it cannot be in the future. That is, we invalidate tokens on the Oba
     * side in order to prevent a client from losing a long-lived token.
     *
     * The jti (JWD IT) claim should be set as well. Oba should check that a token is only used once, but this is a
     * bit of a nice to have as the max validity period is already limited to only 20 seconds.
     *
     * The token must contain a key id header
     *
     * @param clientId
     * @return
     */
    public String createRequestToken(String clientId, String kid) {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("client_id", clientId);
        jwtClaims.setClaim("iat", System.currentTimeMillis());
        jwtClaims.setClaim("jti", UUID.randomUUID());

        return createTokenFromClaims(jwtClaims, new HashMap<String, String>() {
            {
                put("kid", kid);
            }
        });
    }
}
