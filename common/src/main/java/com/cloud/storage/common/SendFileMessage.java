package com.cloud.storage.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SendFileMessage extends CommonMessage{
    public String filePath;
    public String fileName;
    public byte[] bFile;

    public SendFileMessage(String sessionId ,String filePath) throws Exception{
        super(sessionId);
        this.filePath = filePath;
        Path p = Paths.get(filePath);
        this.bFile = Files.readAllBytes(Paths.get(filePath));
        this.fileName = p.getFileName().toString();
        super.setMessageId(3);
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getbFile() {
        return bFile;
    }
}
