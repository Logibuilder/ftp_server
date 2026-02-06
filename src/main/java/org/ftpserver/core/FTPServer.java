package org.ftpserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Serveur FTP principal gérant l'écoute réseau.
 * Cette classe initialise le socket d'accueil et boucle indéfiniment pour accepter
 * de nouvelles connexions clients, chacune étant traitée dans un thread séparé.
 */
public class FTPServer {
    private final int port;

    /**
     * @param port Le port TCP sur lequel le serveur doit écouter.
     */
    public FTPServer(int port) {
        this.port = port;
    }

    /**
     * Démarre la boucle infinie d'acceptation des clients.
     */
    public void start() throws IOException {

        try(ServerSocket welcomeSocket = new ServerSocket(this.port)){
            System.out.println("Serveur FTP en attente sur le port " + port+"...");
            while(true) {
                Socket socket = welcomeSocket.accept();
                FTPSocket controllerSocket = new FTPSocket(socket);

                new Thread(new Client(controllerSocket)).start();
            }
        }
    }
}
