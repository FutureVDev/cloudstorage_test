package com.cloud.storage.client;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SessionData {

    public static String sessionId;
    public static File clientFileDir = new File("client/client_storage/");
    public static File[] clientFileList = clientFileDir.listFiles();
    public static File[] serverFileList;

    public static List<String> getLocalFileNames(){
        List<String> results = new ArrayList<String>();
        for (File file : clientFileList) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }

    public static List<String> getServerFileNames(){
        List<String> results = new ArrayList<String>();
        for (File file : serverFileList) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }

    public static void updateLocalFiles(){
        clientFileList = clientFileDir.listFiles();
    }
}
