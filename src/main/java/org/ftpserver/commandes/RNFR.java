package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implémentation du protocole de renommage en deux étapes.
 * RNFR (Rename From) : Mémorise la source du renommage dans la session client.
 */
public class RNFR implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        if (args.length == 0) {
            client.getControllerSocket().write("501 Erreur syntaxe : RNFR nom_fichier");
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
        // Vérifications de sécurité via Files
        if (!target.startsWith(root)) {
            client.getControllerSocket().write("550 accès refusé.");
            return;
        }

        client.setNameFrom(target.toString());
        client.getControllerSocket().write("350 Pret pour RNTO");
    }
}
