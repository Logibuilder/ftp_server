package org.ftpserver;

import org.ftpserver.core.FTPServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FTPServer server = new FTPServer(8081);
        server.start();
    }
}