package com.amenah.tareq.project1;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageManager {


    private static final String MAIN_FOLDER_NAME = "ChatI App";
    private static final String IMAGE_FOLDER_NAME = MAIN_FOLDER_NAME + File.separator + "Media" + File.separator + "Images";
    private static final String BINARY_FILE_FOLDER_NAME = MAIN_FOLDER_NAME + File.separator + "Media" + File.separator + "Binary files";


    public static void saveMessage() {

    }

    public static void saveImage(byte[] fileBytes, String sender, String ext) {

        String path = IMAGE_FOLDER_NAME;

        saveFile(fileBytes, sender, ext, path);

    }


    public static void saveBinaryFile(byte[] fileBytes, String sender, String ext) {

        String path = BINARY_FILE_FOLDER_NAME;

        saveFile(fileBytes, sender, ext, path);

    }


    public static void saveFile(byte[] fileBytes, String sender, String ext, String path) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String sentDate = dateFormat.format(date);

        try {

            String folderPath = getFolder(path);
            String fileName = String.valueOf((sender + sentDate).hashCode()) + ext;
            File image = new File(folderPath, fileName);
            OutputStream outputStream = new FileOutputStream(image);
            outputStream.write(fileBytes);
            outputStream.close();

        } catch (FileNoteCreatedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static String getFolder(String fname) throws FileNoteCreatedException {

        String myfolder = Environment.getExternalStorageDirectory() + File.separator + fname;
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


}
