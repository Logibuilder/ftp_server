package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;

/**
 * Implémentation de la commande QUIT .
 * <p>
 * Cette commande permet au client de demander la fermeture de la session FTP.
 * Le serveur répond par un code 221 puis ferme les connexions réseau associées.
 * </p>
 * <p>
 * Lors de l'exécution, cette commande assure la libération des ressources en fermant
 * le socket de contrôle et le socket de données s'il était ouvert.
 * </p>
 */
public class QUIT implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) {

        try {
            client.getControllerSocket().write("221 Au revoir.");

            client.getControllerSocket().close();

            if (client.getDataSocket() != null) {
                client.getDataSocket().close();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture : " + e.getMessage());
        }
    }
}
