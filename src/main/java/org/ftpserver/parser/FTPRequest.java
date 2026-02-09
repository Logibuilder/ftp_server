package org.ftpserver.parser;


/**
 * Objet de transfert de données représentant une requête FTP analysée.
 * <p>
 * Contient le verbe de la commande (ex: "USER") et ses arguments associés,
 * extraits par le {@link CommandeParser}.
 * </p>
 */
public class FTPRequest {
    private String commande;
    private String[] args;

    public FTPRequest(String cmd, String[] args){
        this.commande = cmd;
        this.args = args;
    }


    public String getCommande() {
        return commande;
    }

    public String[] getArgs() {
        return args;
    }
}
