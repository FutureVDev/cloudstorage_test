package com.cloud.storage.client;

import com.cloud.storage.common.CommonMessage;
import com.cloud.storage.common.FileListMessage;
import com.cloud.storage.common.LoginMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;

public class LoginController {
    @FXML
    VBox loginNode;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;


    public void handleAuth(){
        try{
            CSConnection cn = new CSConnection();
            cn.connectToServer();
            LoginMessage lm = new LoginMessage(loginField.getText(),passwordField.getText());
            cn.sendMessage(lm);
            CommonMessage cm = (CommonMessage)cn.getIn().readObject();
            System.out.println("# Got common message");
            System.out.println("# Message id: " + cm.getMessageId());
            if(cm.getMessageId() == 5){
                System.out.println("# Got FileListMessage");
                FileListMessage flm = (FileListMessage) cm;
                SessionData.sessionId = flm.getSessionId();
                System.out.println("# New sessionId = " + SessionData.sessionId);
                SessionData.serverFileList = flm.getFileList();
                System.out.println("# ServerFileList: " + Arrays.toString(SessionData.serverFileList));
                changeSceneToMain();
            }
            if(cm.getMessageId() == 6){
                System.out.println("# Wrong login");
            }
        }catch (Exception e){
            System.out.println("# Could not connect to server");
            System.out.println(e);
        }
        loginField.clear();
        passwordField.clear();

    }

    public void changeSceneToMain() {
        try {
            Parent mainScene = FXMLLoader.load(getClass().getResource("/main.fxml"));
            ((Stage) loginNode.getScene().getWindow()).setScene(new Scene(mainScene));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
