package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;
import org.ftpserver.core.FTPSocket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Implémentation de la commande STOR.
 * Permet au client de téléverser un fichier vers le serveur.
 * Gère la création récursive des répertoires parents si nécessaire avant l'écriture.
 */
public class STOR implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        if (args.length == 0) {
            client.getControllerSocket().write("550 Erreur syntaxe : STOR nom_fichier");
            return;
        }

        if (client.getDataSocket() == null) {
            client.getControllerSocket().write("503 Utilisez PASV avant STOR.");
            return;
        }

        Path root = Paths.get(".").toAbsolutePath().normalize();
        String targetStr = args[0].startsWith("/") ? args[0].substring(1) : args[0];
        Path target = root.resolve(client.getCurrentPath().substring(1)).resolve(targetStr).normalize();

        if (!target.startsWith(root)) {
            client.getControllerSocket().write("550 Accès refusé.");
            return;
        }

        client.getControllerSocket().write("150 Pet a recvoir les donnees");

        try {
            // On accepte la connexion de données (établie via PASV)
            FTPSocket dataChannel = client.getDataSocket().accept();
            BufferedInputStream in = dataChannel.getInputStream();

            //creer le repertoire s'il n'existe pas
            if (target.getParent() != null && !Files.exists(target.getParent())) {
                Files.createDirectories(target.getParent());
            }

            // Copie du flux sur leLecture du fichier et écriture sur le socket fichier/ remplacer si existant
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);

            // Fermeture du canal de données pour signaler la fin du fichier
            dataChannel.close();
            client.getDataSocket().close();
            client.setDataSocket(null);

            client.getControllerSocket().write("226 Transfert réussi.");
        } catch (Exception e) {
            // Capturer TOUTES les exceptions
            client.getControllerSocket().write("425 Erreur lors de l'upload :" + e.getMessage());
        }
    }
}
