package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

public class SYST implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) {

        client.getControllerSocket().write("215  Type UNIX: L8");
    }
}
