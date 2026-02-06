package org.ftpserver.parser;

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
