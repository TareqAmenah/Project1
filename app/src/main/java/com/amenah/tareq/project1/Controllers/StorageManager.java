package com.amenah.tareq.project1.Controllers;

import android.os.Environment;
import android.util.Log;

import com.amenah.tareq.project1.ConnectionManager.Messages.Message;
import com.amenah.tareq.project1.Encryption.AESUtil;
import com.amenah.tareq.project1.MyCustomPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class StorageManager {


    private static final String MAIN_FOLDER_NAME = "ChatI App";
    private static final String CHAT_HISTORY_FOLDER_NAME = MAIN_FOLDER_NAME + File.separator + "Chat history";
    private static final String IMAGE_FOLDER_NAME = MAIN_FOLDER_NAME + File.separator + "Media" + File.separator + "Images";
    private static final String BINARY_FILE_FOLDER_NAME = MAIN_FOLDER_NAME + File.separator + "Media" + File.separator + "Binary files";
    private static final String CHATS_LIST_FILE = "ChatsList.slz";
    private static final String USER_DETAILS_FILE = "UserDetails.slz";


    public static void saveFriendChat(String receiver) {

        List<Message> friendChat = UserModule.getChatsOf(receiver);
        try {
            String filePath = getFolder(CHAT_HISTORY_FOLDER_NAME + File.separator + UserModule.getUsername());
            File fullFilePath = new File(filePath, UserModule.getUsername() + "#" + receiver + ".slz");

            FileOutputStream fileOutputStream = new FileOutputStream(fullFilePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(friendChat);
            objectOutputStream.close();
            fileOutputStream.close();

            Log.v("***************", "Object has been serialized");

        } catch (FileNoteCreatedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveChatsList() {

        List<MyCustomPair> chatList = new ArrayList<>();

        if (UserModule.getFriendsList() == null)
            return;

        for (String s : UserModule.getFriendsList()) {
            List<Message> list = UserModule.getChatsOf(s);
            if (list != null && list.size() != 0) {
                if (list.size() == 1)
                    chatList.add(new MyCustomPair(s, list.get(0).getText()));
                else
                    chatList.add(new MyCustomPair(s, list.get(list.size() - 1).getText()));
            }
        }

        String filePath = null;
        try {
            filePath = getFolder(CHAT_HISTORY_FOLDER_NAME + File.separator + UserModule.getUsername());
            File fullFilePath = new File(filePath, CHATS_LIST_FILE);

            FileOutputStream fileOutputStream = new FileOutputStream(fullFilePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(chatList);
            objectOutputStream.close();
            fileOutputStream.close();

            Log.v("***************", "Object has been serialized");

        } catch (FileNoteCreatedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String saveImage(byte[] fileBytes, String sender, String ext) {

        String path = IMAGE_FOLDER_NAME;

        //decrypt the image
        String secretKey = UserModule.getSecretKeyOfFriend(sender);
        try {
            byte[] decryptedImage = AESUtil.decrypt(fileBytes, secretKey);
            return saveFile(decryptedImage, sender, ext, path);

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return saveFile(fileBytes, sender, ext, path);

    }

    public static String saveBinaryFile(byte[] fileBytes, String sender, String ext) {

        String path = BINARY_FILE_FOLDER_NAME;

        //decrypt the file
        String secretKey = UserModule.getSecretKeyOfFriend(sender);
        try {
            byte[] decryptedFile = AESUtil.decrypt(fileBytes, secretKey);
            return saveFile(decryptedFile, sender, ext, path);

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return saveFile(fileBytes, sender, ext, path);

    }

    private static String saveFile(byte[] fileBytes, String sender, String ext, String path) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = new Date();
        String sentDate = dateFormat.format(date);

        try {

            String folderPath = getFolder(path);
            String fileName = String.valueOf((sender + sentDate).hashCode()) + ext;
            File file = new File(folderPath, fileName);
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileBytes);
            outputStream.close();
            return folderPath + File.separator + fileName;

        } catch (FileNoteCreatedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getFolder(String fName) throws FileNoteCreatedException {

        String myfolder = Environment.getExternalStorageDirectory() + File.separator + fName;
        File f = new File(myfolder);
        if (!f.exists())
            if (!f.mkdirs()) {
                //file can't be created
                throw new FileNoteCreatedException(myfolder);
            } else {
                // file created
                return myfolder;
            }
        else {
            // file is exist
            return myfolder;

        }
    }

    public static List<Message> getFriendChat(String friendName) {

        String path = Environment.getExternalStorageDirectory() + File.separator +
                CHAT_HISTORY_FOLDER_NAME + File.separator + UserModule.getUsername()
                + File.separator + (UserModule.getUsername() + "#" + friendName + ".slz");

        File file = new File(path);
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileInputStream);
                return (ArrayList<Message>) in.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        return new ArrayList<>();

    }

    public static List<MyCustomPair> getChatsList() {
        String path = Environment.getExternalStorageDirectory() + File.separator +
                CHAT_HISTORY_FOLDER_NAME + File.separator + UserModule.getUsername()
                + File.separator + CHATS_LIST_FILE;

        File file = new File(path);
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileInputStream);
                ArrayList<MyCustomPair> x = (ArrayList<MyCustomPair>) in.readObject();
                return x;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        return null;

    }

    public static void deleteFriendChat(String friendName) {

        String path = Environment.getExternalStorageDirectory() + File.separator +
                CHAT_HISTORY_FOLDER_NAME + File.separator + UserModule.getUsername()
                + File.separator + (UserModule.getUsername() + "#" + friendName + ".slz");

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

    }

    public static void deleteAllFriendsChat() {

        deleteChatsList();

        List<String> friendsList = UserModule.getFriendsList();
        if (friendsList != null) {
            for (String s : friendsList) {
                if (s != null)
                    deleteFriendChat(s);
            }
        }


        String path = Environment.getExternalStorageDirectory() + File.separator +
                CHAT_HISTORY_FOLDER_NAME + File.separator + UserModule.getUsername();

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }


    }

    public static void deleteChatsList() {
        String path = Environment.getExternalStorageDirectory() + File.separator +
                CHAT_HISTORY_FOLDER_NAME + File.separator + UserModule.getUsername()
                + File.separator + CHATS_LIST_FILE;

        File file = new File(path);
        if (file.exists())
            file.delete();
    }

    public static void saveUser(UserForSaving userForSaving) {

        String filePath = null;
        try {
            filePath = getFolder(MAIN_FOLDER_NAME);
            File fullFilePath = new File(filePath, USER_DETAILS_FILE);

            FileOutputStream fileOutputStream = new FileOutputStream(fullFilePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(userForSaving);
            objectOutputStream.close();
            fileOutputStream.close();

            Log.v("***************", "Object has been serialized");

        } catch (FileNoteCreatedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static UserForSaving getUser() {

        String path = Environment.getExternalStorageDirectory() + File.separator +
                MAIN_FOLDER_NAME + File.separator + USER_DETAILS_FILE;

        File file = new File(path);
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileInputStream);
                UserForSaving x = (UserForSaving) in.readObject();
                return x;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    public static void deleteUser() {

        String path = Environment.getExternalStorageDirectory() + File.separator +
                MAIN_FOLDER_NAME + File.separator + USER_DETAILS_FILE;

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

    }

    public static void clearAll() {
        deleteAllFriendsChat();
        deleteChatsList();
        deleteUser();
    }

}
