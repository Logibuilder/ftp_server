package org.ftpserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Client implements Runnable{

    FTPSocket controllerSocket;
    FTPSocket dataSocket;

    public Client(FTPSocket socketControl) {
        controllerSocket = socketControl;
    }

    @Override
    public void run() {
        try {
            controllerSocket.write("220 Welcome to  FTPServer");
            authenticate();
            controllerSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                controllerSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void authenticate() throws IOException {

        String commandeUser = controllerSocket.read();
        if (commandeUser == null) return;

        String[] userParts = commandeUser.split(" ", 2);

        if (!userParts[0].equals("USER") || userParts.length < 2) {
            controllerSocket.write("530 Please login with USER and PASS.");
            return;
        }

        String userName = userParts[1];

        controllerSocket.write("331 Nom d'utilisateur reçu, mot de passe demandé");
        String commandePassword = controllerSocket.read();

        if (commandePassword == null) return;

        String[] passwordParts = commandePassword.split(" ", 2);

        if (!passwordParts[0].equals("PASS") || passwordParts.length <2) {
            controllerSocket.write("530 Please provide password.");
            return;
        }

        String password = passwordParts[1];

        if (!checkPUser(userName, password)) {
            controllerSocket.write("530 login ou password incorrect.");
        } else {
            controllerSocket.write("230 User logged in, proceed.");
        }

    }

    private boolean checkPUser(String userNale, String password) {
        return true;
    }


}
