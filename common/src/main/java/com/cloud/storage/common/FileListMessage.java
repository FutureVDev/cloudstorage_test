package com.cloud.storage.common;

import java.io.File;

public class FileListMessage extends CommonMessage {
    File[] fileList;

    public FileListMessage(String sessionId, File[] fileList) {
        super(sessionId);
        this.fileList = fileList;
        setMessageId(5);
    }

    public FileListMessage(File[] fileList) {
        this.fileList = fileList;
        setMessageId(5);
    }

    public File[] getFileList() {
        return fileList;
    }
}
