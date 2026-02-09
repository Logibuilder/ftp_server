package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

/**
 * Implémentation de la commande SYST (System).
 * <p>
 * Informe le client sur le type de système d'exploitation du serveur
 * pour optimiser les transferts et le listage des fichiers.
 * </p>
 */
public class SYST implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) {

        client.getControllerSocket().write("215  Type UNIX: L8");
    }
}
