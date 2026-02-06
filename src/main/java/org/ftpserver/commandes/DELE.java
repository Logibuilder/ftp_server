package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DELE implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        if (args.length == 0) {
            client.getControllerSocket().write("501 Erreur syntaxe : DELE nom_fichier");
            return;
        }

        Path root = Paths.get(".").toAbsolutePath().normalize();
        String targetStr = args[0].startsWith("/") ? args[0].substring(1) : args[0];
        Path target = root.resolve(client.getCurrentPath().substring(1)).resolve(targetStr).normalize();

        // Vérifications d'existence  via Files
        if (!Files.exists(target) ) {
            client.getControllerSocket().write("550 Fichier inexistant.");
            return;
        }

        if (!target.startsWith(root)) {
            client.getControllerSocket().write("550 Accès refusé.");
            return;
        }
        try {
            if (Files.isDirectory(target)) {
                client.getControllerSocket().write("550 Utilisez RMD pour supprimer un repertoire.");
                return;
            }
            Files.delete(target);
            client.getControllerSocket().write("250 " + target +" supprime avec succes.");
        } catch (IOException e)  {
            client.getControllerSocket().write("550 Impossible de supprimer le fichier : " + e.getMessage());
        }
    }
}
