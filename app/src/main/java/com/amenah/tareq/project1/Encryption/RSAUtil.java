package com.amenah.tareq.project1.Encryption;

import android.util.Base64;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class RSAUtil {

    private static String publicKey;
    private static String privateKey;

    public static String encrypt(String data, String publicKey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return Base64.encodeToString(cipher.doFinal(data.getBytes()), Base64.NO_WRAP);
    }

    public static String encrypt(String data, PublicKey publicKey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeToString(cipher.doFinal(data.getBytes()), Base64.DEFAULT);
    }

    public static String decrypt(byte[] data, String base64PrivateKey) throws GeneralSecurityException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(base64PrivateKey));
        String s = new String(cipher.doFinal(data));
        return s;
    }

    public static String decrypt(String data, String base64PrivateKey) throws GeneralSecurityException {
        return decrypt(Base64.decode(data.getBytes(), Base64.DEFAULT), base64PrivateKey);
    }


    private static PublicKey getPublicKey(String base64PublicKey) {
        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(base64PublicKey.getBytes(), Base64.NO_WRAP));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    private static PrivateKey getPrivateKey(String base64PrivateKey) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(base64PrivateKey.getBytes(), Base64.DEFAULT));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }


    public static String RSAPublicKeyToXML(String base64PublicKey) {
        PublicKey publicKey = getPublicKey(base64PublicKey);
        String _NETKey = "";
        try {
            // Get the bytes of the public and private keys.
            byte[] publicKeyBytes = publicKey.getEncoded();

            //Generate Public Key and Public Key in XML format.
            RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").
                    generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            //store the public key in XML string to make compatible .Net public key file
            _NETKey = getRSAPublicKeyAsXMLString(rsaPublicKey);
            //System.out.println(_NETKey);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.out.println(e.getMessage());
        } catch (TransformerException e) {
            System.out.println(e.getMessage());
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        } finally {
            return _NETKey;
        }
    }

    private static String getRSAPublicKeyAsXMLString(RSAPublicKey key) throws ParserConfigurationException, TransformerException {
        org.w3c.dom.Document xml = RSAPublicKeyToXML(key);
        Transformer transformer =
                TransformerFactory.newInstance().newTransformer();
        StringWriter sw = new StringWriter();
        transformer.transform(new DOMSource(xml), new StreamResult(sw));
        return sw.getBuffer().toString();

    }

    private static org.w3c.dom.Document RSAPublicKeyToXML(RSAPublicKey key) throws ParserConfigurationException {
        org.w3c.dom.Document result = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        org.w3c.dom.Element rsaKeyValue = result.createElement("RSAKeyValue");
        result.appendChild(rsaKeyValue);
        org.w3c.dom.Element modulus = result.createElement("Modulus");
        rsaKeyValue.appendChild(modulus);

        byte[] modulusBytes = key.getModulus().toByteArray();
        modulusBytes = stripLeadingZeros(modulusBytes);
        modulus.appendChild(result.createTextNode(Base64.encodeToString(modulusBytes, Base64.DEFAULT)));

        org.w3c.dom.Element exponent = result.createElement("Exponent");
        rsaKeyValue.appendChild(exponent);

        byte[] exponentBytes = key.getPublicExponent().toByteArray();
        exponent.appendChild(result.createTextNode(Base64.encodeToString(exponentBytes, Base64.DEFAULT)));

        return result;
    }

    private static byte[] stripLeadingZeros(byte[] a) {
        int lastZero = -1;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == 0) {
                lastZero = i;
            } else {
                break;
            }
        }
        lastZero++;
        byte[] result = new byte[a.length - lastZero];
        System.arraycopy(a, lastZero, result, 0, result.length);
        return result;
    }

    /**
     * Convert C# RSA Public Key to Java Public Key.
     *
     * @param XML C# XML RSA Key.
     * @return Java Public Key.
     */
    public static PublicKey RSAPublicKeyFromXML(String XML) {
        PublicKey pubKey = null;
        try {
            DocumentBuilderFactory Dfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = Dfactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(XML));
            org.w3c.dom.Document w3cDocument = documentBuilder.parse(is);
            Document document = new DOMBuilder().build(w3cDocument);

            Element root = document.getRootElement();

            Element modulusElem = root.getChild("Modulus");
            Element exponentElem = root.getChild("Exponent");

            byte[] expBytes = Base64.decode(exponentElem.getText().trim(), Base64.NO_WRAP);
            byte[] modBytes = Base64.decode(modulusElem.getText().trim(), Base64.NO_WRAP);

            BigInteger modules = new BigInteger(1, modBytes);
            BigInteger exponent = new BigInteger(1, expBytes);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponent);
            pubKey = factory.generatePublic(pubSpec);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            return pubKey;
        }
    }

/*
    public static void DOCUMENTATION(){
        //Generates Java RSA Keys
        KeyPair keypair = RSAOperation.generateKeys(2048);

        //Assign Java Keys.
        PrivateKey privateKey = keypair.getPrivate();
        PublicKey publicKey = keypair.getPublic();

        //Convert to C# Public Key.
        String XML = RSAOperation.RSAPublicKeyToXML(publicKey);

        //Retrieve RSA Public Key from XML String
        publicKey = RSAOperation.RSAPublicKeyFromXML(XML);
    }
*/

}
