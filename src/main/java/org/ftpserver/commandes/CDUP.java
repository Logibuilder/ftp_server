package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;

public class CDUP implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        new CWD().execute(new String[]{".."}, client);
    }
}
