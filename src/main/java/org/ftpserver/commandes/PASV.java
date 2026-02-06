package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;
import org.ftpserver.core.FTPSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;


/**
 * Implémentation de la commande PASV.
 * Demande au serveur d'écouter sur un port éphémère pour établir une connexion de données.
 * Retourne l'adresse IP et le port formatés .
 */
public class PASV implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {

        try {
            //précondition : fermer l'ancier socket de données s'il exixste
            if (client.getDataSocket() != null && !client.getDataSocket().isClosed()) {
                client.getDataSocket().close();
            }

            ServerSocket ds = new ServerSocket(0);

            client.setDataSocket(new FTPSocket(ds));

            int port = ds.getLocalPort();
            int p1 = port / 256;
            int p2 = port % 256;

            // RÉCUPÉRATION DYNAMIQUE DE L'IP
            // InetAddress.getLocalHost() récupère l'adresse de la machine locale
            String ip = InetAddress.getLocalHost().getHostAddress().replace(".", ",");
            String reponse = "227 entrée en mode passif (" + ip + "," + p1 + "," + p2 + ").";
            client.getControllerSocket().write(reponse);
        } catch (IOException e) {
            client.getControllerSocket().write("425 Erreur lors du du passage en mode passif");
            throw  e;
        }
    }
}
