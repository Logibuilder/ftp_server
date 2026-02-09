package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;


/**
 * Implémentation de la commande TYPE.
 * <p>
 * Définit le mode de transfert des données (ASCII ou Image/Binaire).
 * Le serveur privilégie le mode Image (I) pour garantir l'intégrité des fichiers binaires.
 * </p>
 */
public class TYPE implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) {
        // args[0] contient souvent 'I' pour binaire ou 'A' pour ASCII
        client.getControllerSocket().write("200 Type binaire I.");
    }
}
