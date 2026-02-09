package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;

/**
 * Implémentation de la commande CDUP (Change to Parent Directory).
 * <p>
 * Commande permettant de remonter d'un niveau dans
 * l'arborescence des répertoires en appelant la logique de {@link CWD}.
 * </p>
 */
public class CDUP implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        new CWD().execute(new String[]{".."}, client);
    }
}
