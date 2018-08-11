package com.cloud.storage.common;


import java.io.Serializable;

abstract public class CommonMessage implements Serializable {

    public int messageId;
    //1 login
    //2 request
    //3 send
    //4 logout
    //5 file list
    //6 fail
    public String sessionId;

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public CommonMessage() {
    }

    public CommonMessage( String sessionId) {
        this.sessionId = sessionId;
    }

}
