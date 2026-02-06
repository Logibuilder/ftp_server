package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

public class PWD implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) {
        client.getControllerSocket().write("257 \""+ client.getCurrentPath() + "\" est le répertoire actuel.");
    }
}
