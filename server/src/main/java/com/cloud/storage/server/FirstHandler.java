package com.cloud.storage.server;

//import com.cloud.storage.common.CommonMessage;
import com.cloud.storage.common.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.UUID;

public class FirstHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        File[] clientFileList;
        Connection connection;
        String client;
        SqlHandler sqlH = new SqlHandler();
        if (msg == null) {
            System.out.println("fail");//TODO handle errors better
            return;
        }
        //1 login
        //2 request
        //3 send
        //4 logout
        //5 file list
        //6 fail
        CommonMessage cm = (CommonMessage) msg;
        System.out.println("# Got common message");
        if(cm.getMessageId() == 1){
            System.out.println("# Login message");
            LoginMessage lm = (LoginMessage) cm;
            client = lm.getLogin();
            if(lm.getPassword().equals(sqlH.getPasswordFromUser(client))){ //TODO hash password
                String uniqueID = UUID.randomUUID().toString();
                sqlH.setSessionForUser(uniqueID,lm.getLogin());
                clientFileList = getFileList(lm.getLogin());
                FileListMessage flm = new FileListMessage(uniqueID,clientFileList);
                ctx.write(flm);
            }else {
                FailMessage fm = new FailMessage();
                ctx.write(fm);
            }
        }

        if(cm.getMessageId() == 2){
            System.out.println("# Got request file message");
            RequestFileMessage rm = (RequestFileMessage) cm;
            client = sqlH.getUserFromSession(rm.getSessionId());
            if(!client.equals("fail")){
                clientFileList = getFileList(client);
                for(int i = 0; i < clientFileList.length; i++){
                    if(rm.getFileName().equals(clientFileList[i].getName())) {
                        SendFileMessage sfm = new SendFileMessage(rm.getSessionId(),clientFileList[i].getPath());
                        ctx.write(sfm);
                        break;
                    }
                    if(i == clientFileList.length) {
                        FailMessage fm = new FailMessage();
                        ctx.write(fm);
                    }
                }
            }else{
                FailMessage fm = new FailMessage();
                ctx.write(fm);
            }
        }

        if(cm.getMessageId() == 3){
            System.out.println("# Send file message");
            SendFileMessage sfm = (SendFileMessage) cm;
            client = sqlH.getUserFromSession(sfm.getSessionId());
            if(!client.equals("fail")){
//                clientFileList = getFileList(client); //TODO check if file exists
                try(FileOutputStream fos = new FileOutputStream(getClientDir(client) + "\\" + sfm.getFileName())){
                    fos.write(sfm.getbFile());
                    clientFileList = getFileList(client);
                    FileListMessage flm = new FileListMessage(clientFileList);
                    ctx.write(flm);
                }catch (Exception e){
                    System.out.println(e);
                    FailMessage fm = new FailMessage();
                    ctx.write(fm);
                }
            } else {
                FailMessage fm = new FailMessage();
                ctx.write(fm);
            }
        }

        if(cm.getMessageId() == 4){
            LogoutMessage lom = (LogoutMessage) cm;
            client = sqlH.getUserFromSession(lom.getSessionId());
            if(!client.equals("fail")){
                sqlH.removeSessionForUser(client);
            }
        }

        if(cm.getMessageId() == 5){
            FileListMessage flm = (FileListMessage) cm;
            System.out.println("something went wrong");
        }
        ctx.flush();
    }

    private File[] getFileList(String userName) {
        File clientFileDir = new File(getClientDir(userName));
        File[] clientFileList = clientFileDir.listFiles();
        return clientFileList;
    }

    private String getClientDir(String userName){
        String clientDir = "C:/Users/1/Desktop/cloudServerFileHolder/server/" + userName; //TODO use relative path
        return clientDir;
    }
}
