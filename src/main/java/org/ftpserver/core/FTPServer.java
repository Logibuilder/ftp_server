package org.ftpserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Serveur FTP principal gérant l'écoute réseau.
 * Cette classe initialise le socket d'accueil et boucle indéfiniment pour accepter
 * de nouvelles connexions clients, chacune étant traitée dans un thread séparé.
 */
public class FTPServer {
    private final int port;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(100);

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

                threadPool.execute(new Client(controllerSocket));
            }
        }
    }
}
