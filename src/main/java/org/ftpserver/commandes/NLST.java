package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;
import org.ftpserver.core.FTPSocket;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Commande NLST (Name List).
 * Transfère une liste simplifiée (noms uniquement) des fichiers du répertoire courant.
 */
public class NLST implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        // Vérifier que le canal de données est prêt (établi par PASV)
        if (client.getDataSocket() == null) {
            client.getControllerSocket().write("503 Utilisez PASV avant NLST.");
            return;
        }

        try {
            client.getControllerSocket().write("150 Ouverture de la connexion de donnees pour la liste des noms.");

            // Acceptation de la connexion sur le socket de données
            FTPSocket dataChannel = client.getDataSocket().accept();

            // Résolution du répertoire actuel du client
            Path dir = Paths.get("." + client.getCurrentPath());

            // Parcours du répertoire via NIO DirectoryStream
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path entry : stream) {
                    // On n'envoie  le nom brut
                    dataChannel.write(entry.getFileName().toString());
                }
            }

            // Fermeture propre et réinitialisation de l'état
            dataChannel.close();
            client.getDataSocket().close();
            client.setDataSocket(null); // pour forcer un nouveau PASV au prochain transfert

            client.getControllerSocket().write("226 Transfert termine avec succes.");

        } catch (IOException e) {
            client.getControllerSocket().write("425 Erreur lors du transfert des noms : " + e.getMessage());
        }
    }
}