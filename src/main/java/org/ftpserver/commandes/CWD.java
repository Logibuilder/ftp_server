package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implémentation de la commande CWD (Change Working Directory).
 * Permet de changer le répertoire de travail actuel.
 * Sécurité : Utilise la normalisation et la vérification de préfixe pour garantir
 * que le client ne sort pas de la racine du serveur (Root Jail).
 */
public class CWD implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        if (args.length == 0) {
            client.getControllerSocket().write("501 Syntaxe : CWD <chemin>");
            return;
        }

        Path root = Paths.get(".").toAbsolutePath().normalize();
        String targetStr = args[0];
        Path target;

        // Chemin absolu FTP vs Relatif
        if (targetStr.startsWith("/")) {
            // Si absolu, on repart de la racine du serveur (.)
            target = root.resolve(targetStr.substring(1)).normalize();
        } else {
            // Si relatif, on part du repertoire actuel (currentPath)
            Path currentPath = root.resolve(client.getCurrentPath().substring(1)).normalize();
            target = currentPath.resolve(targetStr).normalize();
        }

        // Empecher de sortir du dossier projet
        if (!target.startsWith(root)) {
            client.getControllerSocket().write("550 Acces refuse : hors de la racine.");
            return;
        }

        if (Files.exists(target) && Files.isDirectory(target)) {
            // Conversion du chemin physique en chemin virtuel pour le client (ex: /src)
            String newVirtual = "/" + root.relativize(target).toString().replace("\\", "/");
            if (newVirtual.equals("/.")) newVirtual = "/";

            client.setCurrentPath(newVirtual);
            client.getControllerSocket().write("250 Repertoire change avec succes.");
        } else {
            client.getControllerSocket().write("550 Repertoire introuvable.");
        }
    }
}