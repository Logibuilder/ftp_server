package org.ftpserver;


import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FTPServer server = new FTPServer();

        server.start(8081);
    }
}