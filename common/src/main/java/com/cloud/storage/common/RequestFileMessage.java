package com.cloud.storage.common;

public class RequestFileMessage extends CommonMessage {
    String fileName;

    public RequestFileMessage(String sessionId, String fileName) {
        super(sessionId);
        this.fileName = fileName;
        super.setMessageId(2);
    }

    public String getFileName() {
        return fileName;
    }
}
