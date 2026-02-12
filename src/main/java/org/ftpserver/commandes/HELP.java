package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.parser.FTPCommande;

import java.io.IOException;

/**
 * Implémentation de la commande HELP.
 * Fournit au client des informations sur les commandes supportées par le serveur.
 */
public class HELP implements FTPCommande {
    @Override
    public void execute(String[] args, Client client) throws IOException {
        if (args.length == 0) {
            // Liste globale des commandes supportées
            String supportedCommands = "USER PASS PWD CWD CDUP LIST NLST RETR STOR MKD DELE RMD RNFR RNTO SIZE MDTM TYPE SYST QUIT HELP PASV";
            client.getControllerSocket().write("214-Les commandes suivantes sont reconnues :");
            client.getControllerSocket().write("    " + supportedCommands);
            client.getControllerSocket().write("214 Fin de l'aide.");
        } else {
            // Aide spécifique pour une commande donnée
            String cmdToHelp = args[0].toUpperCase();
            String helpMessage;

            switch (cmdToHelp) {
                case "USER": helpMessage = "214 Syntaxe: USER <nom_utilisateur>"; break;
                case "PASS": helpMessage = "214 Syntaxe: PASS <mot_de_passe>"; break;
                case "CWD":  helpMessage = "214 Syntaxe: CWD <chemin_repertoire>"; break;
                case "RETR": helpMessage = "214 Syntaxe: RETR <nom_fichier> (Télécharger)"; break;
                case "STOR": helpMessage = "214 Syntaxe: STOR <nom_fichier> (Téléverser)"; break;
                case "PASV": helpMessage = "214 Entre en mode passif pour le transfert de données."; break;
                case "QUIT": helpMessage = "214 Ferme la session FTP."; break;
                default:
                    helpMessage = "214 Commande inconnue. Tapez HELP pour la liste.";
                    break;
            }
            client.getControllerSocket().write(helpMessage);
        }
    }
}