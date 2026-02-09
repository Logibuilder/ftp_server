package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

/**
 * Implémentation de la commande PWD (Print Working Directory).
 * <p>
 * Cette commande permet au client d'obtenir le chemin absolu du répertoire
 * de travail actuel sur le serveur FTP.
 * </p>
 * <p>
 * réponse: 257
 * suivi du chemin entre guillemets.
 * </p>
 */
public class PWD implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) {
        client.getControllerSocket().write("257 \""+ client.getCurrentPath() + "\" est le répertoire actuel.");
    }
}
