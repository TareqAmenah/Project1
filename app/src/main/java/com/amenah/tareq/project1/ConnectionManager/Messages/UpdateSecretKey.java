package com.amenah.tareq.project1.ConnectionManager.Messages;

import com.amenah.tareq.project1.Controllers.UserModule;
import com.amenah.tareq.project1.Encryption.RSAUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;

public class UpdateSecretKey extends Message {

    private String toPlatform;
    private String encryptedSecretKey;

    public UpdateSecretKey(String receiver, String toPlatform, String encryptedSecretKey) {
        this.type = "updateSecretKey";
        this.receiver = receiver;
        this.toPlatform = toPlatform;
        this.encryptedSecretKey = encryptedSecretKey;

    }

    public UpdateSecretKey(JSONObject jsonMessage) {
        try {
            type = jsonMessage.getString("type");
            String from = jsonMessage.getString("from");
            String encryptedSecretKey = jsonMessage.getString("encryptedSecretKey");

            String decryptedSecretKey = RSAUtil.decrypt(encryptedSecretKey, UserModule.getPrivateKey());
            UserModule.setSecretKeyForFriend(from, decryptedSecretKey);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject getJson() {

        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("to", receiver);
            json.put("toPlatform", toPlatform);
            json.put("encryptedSecretKey", encryptedSecretKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
