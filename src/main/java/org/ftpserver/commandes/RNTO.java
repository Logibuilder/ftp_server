package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Implémentation du protocole de renommage en deux étapes.
 * RNTO (Rename To) : Effectue le déplacement vers la nouvelle destination.
 */
public class RNTO implements FTPCommande {
    @Override
    public synchronized void execute(String[] args, Client client) throws IOException {
        try {
            if (args.length == 0) {
                client.getControllerSocket().write("501 Erreur syntaxe : RNFR nom_fichier");
                return;
            }

            String nameFrom = client.getNameFrom();

            if (nameFrom == null) {
                client.getControllerSocket().write("503 Utilisez d'abord RNFR.");
                return;
            }

            Path root = Paths.get(".").toAbsolutePath().normalize();
            String targetStr = args[0].startsWith("/") ? args[0].substring(1) : args[0];
            Path destination = root.resolve(client.getCurrentPath().substring(1)).resolve(targetStr).normalize();


            // Vérifications de sécurité via Files
            if (!destination.startsWith(root)) {
                client.getControllerSocket().write("550 accès refusé.");
                return;
            }

            Path pathFrom = Paths.get(nameFrom);
            Files.move(pathFrom, destination, StandardCopyOption.REPLACE_EXISTING);
            client.setNameFrom(null);
            client.getControllerSocket().write("250 Renommage réussi.");

        } catch (IOException e) {
            client.getControllerSocket().write("550 Erreur lors du renommage.");
        }
    }
}
