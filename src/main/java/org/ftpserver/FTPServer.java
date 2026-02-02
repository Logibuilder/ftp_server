package org.ftpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class FTPServer {


    public ServerSocket welcomeSocket;

    public void start(int port) throws IOException {

        welcomeSocket = new ServerSocket(port);
        while(true) {
            System.out.println("Server listening...");
            Socket socket = welcomeSocket.accept();
            FTPSocket controllerSocket = new FTPSocket(socket);

            new Thread(new Client(controllerSocket)).start();
        }
    }

}
