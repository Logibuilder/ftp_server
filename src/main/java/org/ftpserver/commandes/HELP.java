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
            // Liste globale des commandes supportées basées sur votre répertoire
            String supportedCommands = "USER PASS PWD CWD CDUP LIST NLST RETR STOR MKD DELE RMD RNFR RNTO SIZE MDTM TYPE SYST QUIT HELP PASV";
            client.getControllerSocket().write("214-Les commandes suivantes sont reconnues :");
            client.getControllerSocket().write("    " + supportedCommands);
            client.getControllerSocket().write("214 Fin de l'aide.");
        } else {
            // Aide spécifique pour chaque commande présente dans le projet
            String cmdToHelp = args[0].toUpperCase();
            String helpMessage;

            switch (cmdToHelp) {
                case "USER": helpMessage = "214 Syntaxe: USER <nom_utilisateur>"; break;
                case "PASS": helpMessage = "214 Syntaxe: PASS <mot_de_passe>"; break;
                case "PWD":  helpMessage = "214 Affiche le repertoire de travail actuel."; break;
                case "CWD":  helpMessage = "214 Syntaxe: CWD <chemin> (Change le repertoire de travail)."; break;
                case "CDUP": helpMessage = "214 Remonte au repertoire parent."; break;
                case "LIST": helpMessage = "214 Syntaxe: LIST [<chemin>] (Liste detaillee des fichiers)."; break;
                case "NLST": helpMessage = "214 Syntaxe: NLST [<chemin>] (Liste simple des noms de fichiers)."; break;
                case "RETR": helpMessage = "214 Syntaxe: RETR <nom_fichier> (Telecharger un fichier)."; break;
                case "STOR": helpMessage = "214 Syntaxe: STOR <nom_fichier> (Televerser un fichier)."; break;
                case "MKD":  helpMessage = "214 Syntaxe: MKD <nom_dossier> (Cree un repertoire)."; break;
                case "DELE": helpMessage = "214 Syntaxe: DELE <nom_fichier> (Supprime un fichier)."; break;
                case "RMD":  helpMessage = "214 Syntaxe: RMD <nom_dossier> (Supprime un repertoire)."; break;
                case "RNFR": helpMessage = "214 Syntaxe: RNFR <ancien_nom> (Etape 1 du renommage)."; break;
                case "RNTO": helpMessage = "214 Syntaxe: RNTO <nouveau_nom> (Etape 2 du renommage)."; break;
                case "SIZE": helpMessage = "214 Syntaxe: SIZE <nom_fichier> (Affiche la taille en octets)."; break;
                case "MDTM": helpMessage = "214 Syntaxe: MDTM <nom_fichier> (Affiche la date de modification)."; break;
                case "TYPE": helpMessage = "214 Syntaxe: TYPE <A|I> (Definit le mode de transfert: ASCII ou Binaire)."; break;
                case "SYST": helpMessage = "214 Affiche le type de systeme d'exploitation du serveur."; break;
                case "PASV": helpMessage = "214 Entre en mode passif pour le transfert de donnees."; break;
                case "QUIT": helpMessage = "214 Ferme la session FTP."; break;
                case "HELP": helpMessage = "214 Syntaxe: HELP [<commande>] (Affiche ce message d'aide)."; break;
                default:
                    helpMessage = "214 Commande inconnue. Tapez HELP sans argument pour la liste.";
                    break;
            }
            client.getControllerSocket().write(helpMessage);
        }
    }
}