package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implémentation de la commande MKD (Make Directory).
 * Crée un nouveau répertoire sur le système de fichiers du serveur de manière sécurisée.
 */
public class MKD implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        if (args.length == 0) {
            client.getControllerSocket().write("501 Syntaxe : MKD <nom_dossier>");
            return;
        }

        Path root = Paths.get(".").toAbsolutePath().normalize();
        String targetStr = args[0].startsWith("/") ? args[0].substring(1) : args[0];
        Path target = root.resolve(client.getCurrentPath().substring(1)).resolve(targetStr).normalize();


        if (!target.startsWith(root)) {
            client.getControllerSocket().write("550 Accès refusé.");
            return;
        }

        try {
            Files.createDirectories(target); // Crée le dossier et ses parents si nécessaire
            client.getControllerSocket().write("257 \"" + args[0] + "\" créé avec succès.");
        } catch (IOException e) {
            client.getControllerSocket().write("550 Impossible de créer le répertoire : " + e.getMessage());
        }
    }
}