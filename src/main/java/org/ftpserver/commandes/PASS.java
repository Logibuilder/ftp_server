package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

/**
 * Implémentation de la commande PASS (Password).
 * <p>
 * Cette commande termine le processus d'authentification initié par la commande USER.
 * Elle transmet le mot de passe au serveur pour vérification.
 * </p>
 * <p>
 * Si le mot de passe est valide, l'état du client est mis à jour pour autoriser
 * l'accès aux autres commandes du serveur.
 * </p>
 */
public class PASS implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) {

        if (args.length < 1) {
            client.getControllerSocket().write("501 Erreur de syntaxe : mot de passe manquant.");
            return;
        }

        // On vérifie le mot de passe
        if (client.checkPUser(client.getUserName(), args[0])) {
            client.setAuthenticated(true);
            client.getControllerSocket().write("230 Authentification réussie. Connexion établie.");
        } else {
            client.getControllerSocket().write("530 identifiant ou mot de passe incorrect.");
        }
    }
}
