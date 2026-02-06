package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;
import org.ftpserver.core.FTPSocket;

import java.io.IOException;
import java.nio.file.*;

/**
 * Implémente la commande LIST.
 * Utilise l'API NIO de Java pour lister les fichiers indépendamment de l'OS.
 */
public class LIST implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        if (client.getDataSocket() == null) {
            client.getControllerSocket().write("503 Utilisez PASV avant LIST.");
            return;
        }

        try {
            //debut du transfert
            client.getControllerSocket().write("150 Ouverture de la connexion de donnees.");
            FTPSocket dataSocketTransfert = client.getDataSocket().accept();

            Path dir = Paths.get("." + client.getCurrentPath());
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path entry : stream) {
                    String line = (Files.isDirectory(entry) ? "d" : "-") + "rw-r--r-- 1 user group " + Files.size(entry) + " " + entry.getFileName().toString();
                    dataSocketTransfert.write(line);
                }
            }

            dataSocketTransfert.close();
            client.getDataSocket().close();
            client.setDataSocket(null);
            client.getControllerSocket().write("226 Transfert de la liste termine.");
        } catch (IOException e) {
            client.getControllerSocket().write("425 Erreur lors du listage.");
        }
    }
}