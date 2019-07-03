package com.amenah.tareq.project1.Encryption;

import android.util.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RSAKeysGenerer {

    private String privateKey;
    private String publicKey;

    public RSAKeysGenerer(int keySize) {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(keySize);
            KeyPair pair = keyGen.generateKeyPair();

            privateKey = Base64.encodeToString(pair.getPrivate().getEncoded(), Base64.NO_WRAP);
            publicKey = Base64.encodeToString(pair.getPublic().getEncoded(), Base64.NO_WRAP);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

}
