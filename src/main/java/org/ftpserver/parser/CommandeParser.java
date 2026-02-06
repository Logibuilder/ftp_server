package org.ftpserver.parser;

/**
 * Analyseur syntaxique des requêtes FTP brutes.
 * Découpe la ligne d'entrée pour extraire le nom de la commande (insensible à la casse)
 * et sépare les arguments pour traitement ultérieur.
 */
public class CommandeParser {

    /**
     * Analyse une ligne brute et retourne un objet FTPRequest.
     * @param input La ligne lue sur le socket (ex: "USER john doe")
     * @return Un objet structuré
     */
    public static FTPRequest getFTPRequest(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new FTPRequest("", new String[0]);
        }

        // On sépare la commande du reste (limite à 2 parties)
        String[] parts = input.trim().split("\\s+", 2);
        String command = parts[0].toUpperCase();

        String[] args;
        if (parts.length > 1) {
            // On sépare les arguments par espace
            args = parts[1].split("\\s+");
        } else {
            args = new String[0];
        }

        return new FTPRequest(command, args);
    }
}
