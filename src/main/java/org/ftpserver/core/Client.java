package org.ftpserver.core;

import org.ftpserver.model.User;
import org.ftpserver.parser.CommandeParser;
import org.ftpserver.parser.FTPCommande;
import org.ftpserver.parser.FTPRequest;
import org.ftpserver.commandes.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Représente la session d'un client FTP connecté.
 * Cette classe maintient l'état de la session (authentification, répertoire courant, sockets)
 * et orchestre l'exécution des commandes via un dictionnaire de commande.
 * Implémente {@link Runnable} pour permettre une gestion multi-threadée.
 */
public class Client implements Runnable{

    FTPSocket controllerSocket;
    FTPSocket dataSocket;
    private String userName;
    /** Indique si l'utilisateur a passé l'étape USER et PASS avec succès */
    private boolean isAuthenticated = false;
    /** Le chemin virtuel actuel du client (ex: "/dossier1") */
    private String currentPath = "/";
    private String nameFrom = null;
    private final Map<String, FTPCommande> commandes = new HashMap<>();

    /**
     * @param socketControl Le socket de contrôle
     */
    protected Client(FTPSocket socketControl) {
        controllerSocket = socketControl;
        commandes.put("USER", new USER());
        commandes.put("PASS", new PASS());
        commandes.put("PWD", new PWD());
        commandes.put("SYST", new SYST());
        commandes.put("TYPE", new TYPE());
        commandes.put("QUIT", new QUIT());
        commandes.put("PASV", new PASV());
        commandes.put("LIST", new LIST());
        commandes.put("RETR", new RETR());
        commandes.put("CWD", new CWD());
        commandes.put("CDUP", new CDUP());
        commandes.put("NLST", new NLST());
        commandes.put("MDTM", new MDTM());
        commandes.put("SIZE", new SIZE());
        commandes.put("STOR", new STOR());
        commandes.put("MKD", new MKD());
        commandes.put("DELE", new DELE());
        commandes.put("RMD", new RMD());
        commandes.put("RNTO", new RNTO());
        commandes.put("RNFR", new RNFR());
        commandes.put("HELP", new HELP());
    }

    public void setAuthenticated(boolean auth) {
        this.isAuthenticated = auth;
    }
    protected boolean isAuthenticated() {
        return isAuthenticated;
    }
    public void setUserName(String name) {
        this.userName = name;
    }

    public FTPSocket getControllerSocket() { return controllerSocket; }

    @Override
    public void run() {
        try {
            controllerSocket.write("220 Bienvenue au serveur FTP");

            while(true) {
                String reponse = controllerSocket.read();
                if (reponse == null) {
                    System.out.println("Connexion fermée par le client");
                    break;
                }

                FTPRequest request = CommandeParser.getFTPRequest(reponse);

                FTPCommande ftpCommande = commandes.get(request.getCommande());

                if (ftpCommande != null){
                    try {

                        //vérification de l'authentification
                        if (!isAuthenticated() && !request.getCommande().equals("USER") && !request.getCommande().equals("PASS") && !request.getCommande().equals("HELP")) {
                            controllerSocket.write("530 Veuillez vous connecter d'abord.");
                            continue;
                        } else {


                            System.out.println("Commande reçue " + request.getCommande());
                            ftpCommande.execute(request.getArgs(), this);

                            System.out.println("Commande " + request.getCommande() + " exécutée avec succès");

                            //On vérifie si c'est la commande quite pour casser le while
                            if (request.getCommande().equals("QUIT")) break;
                        }

                    } catch (Exception e) {
                        controllerSocket.write("500 Erreur interne du serveur.");
                    }
                } else {
                    controllerSocket.write("502 cette commande n'est pas implémentée.");
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur de connexion: " + e.getMessage());
        } finally {
            try {
                System.out.println("Fermeture de la connexion");
                controllerSocket.close();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture: " + e.getMessage());
            }
        }
    }

    /**
     * Vérifie les identifiants de l'utilisateur.
     * @param userName Nom d'utilisateur
     * @param password Mot de passe
     * @return true si les identifiants sont valides
     */
    public boolean checkPUser(String userName, String password) {
        return User.isValid(userName, password);
    }


    public String getUserName() {
        return this.userName;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public FTPSocket getDataSocket() {
        return dataSocket;
    }

    public void setDataSocket(FTPSocket ds) {
        this.dataSocket = ds;
    }

    public void setCurrentPath(String curr) {
        currentPath = curr;
    }

    public void setNameFrom(String nameFrom) {
        this.nameFrom = nameFrom;
    }
    public String getNameFrom() {
        return nameFrom;
    }
}