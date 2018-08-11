package com.cloud.storage.client;

import com.cloud.storage.common.CommonMessage;
import com.cloud.storage.common.FileListMessage;
import com.cloud.storage.common.RequestFileMessage;
import com.cloud.storage.common.SendFileMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.text.TabableView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Observable;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    VBox rootNode;

    @FXML
    ListView<String> localFilesList, serverFilesList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateLocalFileList();
        updateServerFileList();
    }

    public void uploadButtonHandler(){
        try{
            CSConnection cn = new CSConnection();
            cn.connectToServer();
            String filePath = SessionData.clientFileDir + "\\" + localFilesList.getSelectionModel().getSelectedItem();
            System.out.println(filePath);
            SendFileMessage sfm = new SendFileMessage(SessionData.sessionId,filePath);
            cn.sendMessage(sfm);
            CommonMessage cm = (CommonMessage)cn.getIn().readObject();
            System.out.println("# Got common message");
            System.out.println("# Message id: " + cm.getMessageId());
            if(cm.getMessageId() == 5){
                System.out.println("# Got FileListMessage");
                FileListMessage flm = (FileListMessage) cm;
                SessionData.serverFileList = flm.getFileList();
                System.out.println("# ServerFileList: " + Arrays.toString(SessionData.serverFileList));
            }
            if(cm.getMessageId() == 6){
                System.out.println("# Could not upload");
            }
        }catch (Exception e){
            System.out.println("# Could not connect to server");
            System.out.println(e);
        }

        updateServerFileList();


    }

    public void downloadButtonHandler(){
        try{
            CSConnection cn = new CSConnection();
            cn.connectToServer();
//            System.out.println(SessionData.clientFileDir);
            String fileName = serverFilesList.getSelectionModel().getSelectedItem();
            System.out.println(fileName);
            RequestFileMessage rfm = new RequestFileMessage(SessionData.sessionId,fileName);
            cn.sendMessage(rfm);
            CommonMessage cm = (CommonMessage)cn.getIn().readObject();
            System.out.println("# Got common message");
            System.out.println("# Message id: " + cm.getMessageId());
            if(cm.getMessageId() == 3){
                SendFileMessage sfm = (SendFileMessage) cm;
                try(FileOutputStream fos = new FileOutputStream(SessionData.clientFileDir + "\\" + sfm.getFileName())){
                    fos.write(sfm.getbFile());
                    SessionData.updateLocalFiles();
                }
            }

            if(cm.getMessageId() == 6){
                System.out.println("# Server could not send file");
            }
        }catch (Exception e){
            System.out.println("# Could not connect to server");
            System.out.println(e);
        }

        updateLocalFileList();

    }

    public void logoutButtonHandler(){
        changeSceneToLogin();
    }

    private void updateLocalFileList(){

        ObservableList<String> localFiles = FXCollections.observableArrayList(SessionData.getLocalFileNames()); //TODO make file[] type work
        localFilesList.setItems(localFiles);
    }

    private void updateServerFileList(){
        ObservableList<String> serverFiles = FXCollections.observableArrayList(SessionData.getServerFileNames());
        serverFilesList.setItems(serverFiles);
    }

    public void changeSceneToLogin() {
        try {
            Parent mainScene = FXMLLoader.load(getClass().getResource("/login.fxml"));
            ((Stage) rootNode.getScene().getWindow()).setScene(new Scene(mainScene));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
