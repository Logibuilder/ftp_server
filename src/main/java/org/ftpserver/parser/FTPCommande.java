package org.ftpserver.parser;

import org.ftpserver.core.Client;

import java.io.IOException;

/**
 * Interface définissant le contrat pour toutes les commandes FTP.
 * Chaque implémentation de cette interface correspond à une commande du protocole.
 */
public interface FTPCommande {


    /**
     * Exécute la logique de la commande FTP.
     * @param args   Tableau d'arguments passés avec la commande (ex: le nom du fichier).
     * @param client L'instance du client effectuant la requête pour accéder à son état.
     * @throws IOException Si une erreur survient lors de la communication réseau ou l'accès disque.
     */
    void execute(String[] args, Client client) throws IOException;
}
