package com.dawn.bgSys.common;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    public static Map<String, Object> verifierToken(String token, String key) {
        Base64 decoder = new Base64();

        byte[] secret = Base64.encodeBase64(key.getBytes());
        Map decodedPayload = null;
        try {
            decodedPayload = new JWTVerifier(secret).verify(token);
        } catch (NoSuchAlgorithmException e) {
            loggerError(e, NoSuchAlgorithmException.class);
        } catch (InvalidKeyException e) {
            loggerError(e, InvalidKeyException.class);
        } catch (IOException e) {
            loggerError(e, IOException.class);
        } catch (SignatureException e) {
            loggerError(e, SignatureException.class);
        } catch (JWTVerifyException e) {
            loggerError(e, JWTVerifyException.class);
        }
        return decodedPayload;
    }

    public static String signerToken(Map<String, Object> claims, String key) {
        Base64 decoder = new Base64();
        JWTSigner signer = new JWTSigner(Base64.encodeBase64(key.getBytes()));

        if (claims == null) {
            claims = new HashMap();
        }

        return signer.sign(claims);
    }

    public static void loggerError(Exception e, Class c) {
        Logger logger = LoggerFactory.getLogger(JWTUtils.class);
        logger.error(e.getMessage());
    }
}