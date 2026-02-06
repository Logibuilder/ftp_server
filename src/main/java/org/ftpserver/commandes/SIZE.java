package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Commande SIZE.
 * Retourne la taille d'un fichier en octets.
 */
public class SIZE implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        if (args.length == 0) {
            client.getControllerSocket().write("501 Syntaxe : SIZE <nom_fichier>");
            return;
        }

        // Résolution sécurisée du chemin (identique à CWD et MDTM)
        Path root = Paths.get(".").toAbsolutePath().normalize();
        String targetStr = args[0].startsWith("/") ? args[0].substring(1) : args[0];
        Path target = root.resolve(client.getCurrentPath().substring(1))
                .resolve(targetStr)
                .normalize();

        // Sécurité : Vérifier que le fichier est bien dans la racine et qu'il existe
        if (!target.startsWith(root) || !Files.exists(target)) {
            client.getControllerSocket().write("550 Fichier introuvable.");
            return;
        }

        // Vérification du type de ressource
        if (Files.isDirectory(target)) {
            client.getControllerSocket().write("550 SIZE ne fonctionne pas sur les repertoires.");
            return;
        }

        try {
            // Récupération de la taille via NIO
            long size = Files.size(target);
            client.getControllerSocket().write("213 " + size);
        } catch (IOException e) {
            client.getControllerSocket().write("550 Erreur lors de la lecture de la taille.");
        }
    }
}