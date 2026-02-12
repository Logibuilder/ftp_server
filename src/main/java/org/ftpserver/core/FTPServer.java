package org.ftpserver.core;

import org.ftpserver.model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Serveur FTP principal gérant l'écoute réseau.
 * <p>
 * Cette classe initialise le socket d'accueil (ServerSocket) et utilise un
 * {@link java.util.concurrent.ExecutorService} (Pool de threads) pour gérer
 * efficacement les connexions simultanées sans saturer les ressources système.
 * </p>
 */
public class FTPServer {
    private final int port;
    /** Pool de threads pour le recyclage des ressources d'exécution */
    private final ExecutorService threadPool = Executors.newFixedThreadPool(100);

    /**
     * @param port Le port TCP sur lequel le serveur doit écouter.
     */
    public FTPServer(int port) {
        this.port = port;
        User.initUser(); // initialiser la liste des joueurs
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
