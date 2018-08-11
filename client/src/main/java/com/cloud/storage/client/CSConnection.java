package com.cloud.storage.client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.net.Socket;

public class CSConnection {
        ObjectEncoderOutputStream out;
        ObjectDecoderInputStream in;

        public CSConnection() {

        }

        public void connectToServer() throws Exception{
                Socket socket = new Socket("localhost", 8189);
                System.out.println("# Connected to server");
                out = new ObjectEncoderOutputStream(socket.getOutputStream());
                in = new ObjectDecoderInputStream(socket.getInputStream());
        }

        public void sendMessage(Object obj) throws Exception{
                System.out.println("# Sending message: " + obj.getClass().toString());
                out.writeObject(obj);
                out.flush();
        }

        public ObjectEncoderOutputStream getOut() {
                return out;
        }

        public ObjectDecoderInputStream getIn() {
                return in;
        }
}
