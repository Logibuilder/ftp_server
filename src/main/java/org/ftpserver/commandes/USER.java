package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

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
