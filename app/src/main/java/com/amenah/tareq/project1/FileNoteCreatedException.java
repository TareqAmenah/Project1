package com.amenah.tareq.project1;

public class FileNoteCreatedException extends Exception {

    private String filePath;


    FileNoteCreatedException(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "File can not created with path: " + filePath + "\n" + super.toString();
    }
}
