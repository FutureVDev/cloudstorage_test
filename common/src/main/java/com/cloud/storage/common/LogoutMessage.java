package com.cloud.storage.common;

public class LogoutMessage extends CommonMessage {
    public LogoutMessage(String sessionId) {
        super(sessionId);
        setMessageId(4);
    }
}
