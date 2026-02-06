package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Commande MDTM (Modification Time).
 * Retourne la date et l'heure de la dernière modification d'un fichier au format UTC.
 * Format de réponse : 213 YYYYMMDDHHMMSS
 */
public class MDTM implements FTPCommande {

    private static final DateTimeFormatter FTP_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.UTC);

    @Override
    public void execute(String[] args, Client client) throws IOException {
        if (args.length == 0) {
            client.getControllerSocket().write("501 Syntaxe : MDTM <nom_fichier>");
            return;
        }

        // Résolution sécurisée du chemin (identique à CWD)
        Path root = Paths.get(".").toAbsolutePath().normalize();
        String targetStr = args[0].startsWith("/") ? args[0].substring(1) : args[0];
        Path target = root.resolve(client.getCurrentPath().substring(1)).resolve(targetStr).normalize();

        // Vérifications d'existence  via Files
        if (!Files.exists(target) ) {
            client.getControllerSocket().write("550 Fichier inexistant.");
            return;
        }

        // Vérifier que le fichier est bien dans la racine
        if (!target.startsWith(root) ) {
            client.getControllerSocket().write("550 Acces refuge.");
            return;
        }

        try {
            // Récupération de la date de modification via NIO
            FileTime modificationTime = Files.getLastModifiedTime(target);
            String timestamp = FTP_DATE_FORMAT.format(modificationTime.toInstant());

            client.getControllerSocket().write("213 " + timestamp);
        } catch (IOException e) {
            client.getControllerSocket().write("550 Erreur lors de la lecture des attributs.");
        }
    }
}