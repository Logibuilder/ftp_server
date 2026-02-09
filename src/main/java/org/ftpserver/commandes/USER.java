package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

/**
 * Implémentation de la commande USER (User Name).
 * <p>
 * Cette commande est généralement la première envoyée par le client après l'établissement
 * de la connexion. Elle permet d'identifier l'utilisateur qui tente de se connecter.
 * </p>
 * <p>
 * Le serveur répond par un code 331 pour demander le mot de passe associé
 * (via la commande PASS).
 * </p>
 */
public class USER implements FTPCommande {

    @Override
    public void execute(String[] args, Client client) {
        if (args.length < 1) {
            client.getControllerSocket().write("501 Erreur de syntax au niveau des paramètre.");
            return;
        }
        client.setUserName(args[0]); // On stocke l'utilisateur dans l'état de la session
        client.getControllerSocket().write("331 Nom d'utilisateur reçu, mot de passe demandé.");
    }
}
