package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

public class TYPE implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) {
        // args[0] contient souvent 'I' pour binaire ou 'A' pour ASCII
        client.getControllerSocket().write("200 Type binaire I.");
    }
}
