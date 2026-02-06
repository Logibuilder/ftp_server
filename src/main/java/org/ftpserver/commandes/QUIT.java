package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;

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
