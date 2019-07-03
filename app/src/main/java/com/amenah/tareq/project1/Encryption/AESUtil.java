package com.amenah.tareq.project1.Encryption;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {

    //    public static String key;
//    public static byte[] keyArray;
    public static byte[] initVector = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

//public methods


//    public static void setKey(String key) {
//        AESUtil.key = key;
//        keyArray = Base64.getDecoder().decode(key);
//    }
//

    public static String generateSecretKey() {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();

        return Base64.encodeToString(secretKey.getEncoded(), Base64.NO_WRAP);
    }


    //public static encrypt method
    public static byte[] encrypt(String messageToEncrypt, String key) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException, java.security.InvalidKeyException {

        byte[] keyArray = Base64.decode(key, Base64.NO_WRAP);
        byte[] plainBytes = messageToEncrypt.getBytes();

        SecretKey secretKey = new SecretKeySpec(keyArray, "AES");
        IvParameterSpec iv = new IvParameterSpec(initVector);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);


        byte[] cipherMessage = cipher.doFinal(plainBytes);

        ///////////////////////////////////////////////////////////////////

        return cipherMessage;
    }

    public static byte[] encrypt(byte[] binaryFile, String key) throws NoSuchPaddingException,
            IllegalBlockSizeException, java.security.InvalidKeyException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException {

        byte[] keyArray = Base64.decode(key, Base64.NO_WRAP);

        SecureRandom secureRandom = new SecureRandom();
        SecretKey secretKey = new SecretKeySpec(keyArray, "AES");
        IvParameterSpec iv = new IvParameterSpec(initVector);

        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);


        byte[] cipherMessage = cipher.doFinal(binaryFile);


        return cipherMessage;
    }

    //public static decrypt method
    public static byte[] decrypt(byte[] cipherMessage, String key) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException, java.security.InvalidKeyException {

        byte[] keyArray = Base64.decode(key, Base64.NO_WRAP);

        SecretKey secretKey = new SecretKeySpec(keyArray, "AES");
        IvParameterSpec iv = new IvParameterSpec(initVector);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);


        byte[] plainText = cipher.doFinal(cipherMessage);

        return plainText;
    }

}
