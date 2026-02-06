package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;
import org.ftpserver.core.FTPSocket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Implémentation de la commande RETR.
 * Permet au client de télécharger un fichier depuis le serveur.
 * Utilise {@link java.nio.file.Files#copy} pour un transfert binaire efficace vers le socket de données.
 */
public class RETR implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        if (client.getDataSocket() == null) {
            client.getControllerSocket().write("503 Utilisez PASV.");
            return;
        }
        // Utilisation de Path au lieu de File
        Path root = Paths.get(".").toAbsolutePath().normalize();
        String targetStr = args[0].startsWith("/") ? args[0].substring(1) : args[0];
        Path target = root.resolve(client.getCurrentPath().substring(1)).resolve(targetStr).normalize();

        // Vérifications d'existence  via Files
        if (!Files.exists(target) ) {
            client.getControllerSocket().write("550 Fichier inexistant.");
            return;
        }
        // Vérifications de sécurité et d'existence via Files
        if (!target.startsWith(root)  || Files.isDirectory(target)) {
            client.getControllerSocket().write("550 Acces refusé.");
            return;
        }

        try {
            client.getControllerSocket().write("150 Ouverture de la connexion de données pour le transfert.");

            // On accepte la connexion de données (établie via PASV)
            FTPSocket dataChannel = client.getDataSocket().accept();
            BufferedOutputStream out = dataChannel.getOutputStream();

            // Lecture du fichier et écriture sur le socket
            Files.copy(target, out);
            out.flush();

            // Fermeture du canal de données pour signaler la fin du fichier
            dataChannel.close();
            client.getDataSocket().close();
            client.setDataSocket(null);

            client.getControllerSocket().write("226 Transfert réussi.");
        } catch (Exception e) {
            // Capturer TOUTES les exceptions
            client.getControllerSocket().write("425 Erreur lors du transfert : " + e.getMessage());
        }
    }
}