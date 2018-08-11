package com.cloud.storage.common;

public class LoginMessage extends CommonMessage {
    String login;
    String password;

    public LoginMessage(String login, String password) {
        this.login = login;
        this.password = password;
        super.setMessageId(1);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
