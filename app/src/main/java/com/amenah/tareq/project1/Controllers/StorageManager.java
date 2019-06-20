package com.amenah.tareq.project1.Controllers;

import android.os.Environment;
import android.util.Log;

import com.amenah.tareq.project1.ConnectionManager.Messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StorageManager {


    private static final String MAIN_FOLDER_NAME = "ChatI App";
    private static final String CHAT_HISTORY_FOLDER_NAME = MAIN_FOLDER_NAME + File.separator + "Chat history";
    private static final String IMAGE_FOLDER_NAME = MAIN_FOLDER_NAME + File.separator + "Media" + File.separator + "Images";
    private static final String BINARY_FILE_FOLDER_NAME = MAIN_FOLDER_NAME + File.separator + "Media" + File.separator + "Binary files";


    public static void saveFriendChat(String receiver, List<Message> friendChat) {

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


    public static String saveImage(byte[] fileBytes, String sender, String ext) {

        String path = IMAGE_FOLDER_NAME;

        return saveFile(fileBytes, sender, ext, path);

    }


    public static String saveBinaryFile(byte[] fileBytes, String sender, String ext) {

        String path = BINARY_FILE_FOLDER_NAME;

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

        List<String> friendsList = UserModule.getFriendsList();
        for (String s : friendsList) {
            deleteFriendChat(s);
        }

        String path = Environment.getExternalStorageDirectory() + File.separator +
                CHAT_HISTORY_FOLDER_NAME + File.separator + UserModule.getUsername();

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }


    }

}
