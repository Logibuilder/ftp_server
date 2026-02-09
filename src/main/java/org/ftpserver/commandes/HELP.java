package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;

/**
 * Implémentation de la commande HELP.
 * <p>
 * Fournit au client des informations sur les commandes supportées par le serveur.
 * Si aucun argument n'est fourni, liste l'ensemble des fonctionnalités disponibles.
 * </p>
 */
public class HELP implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {

    }
}
