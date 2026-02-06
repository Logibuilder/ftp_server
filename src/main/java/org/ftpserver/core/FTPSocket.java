package org.ftpserver.core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Encapsule les communications réseau FTP.
 * Gère soit un canal de contrôle établi, soit un serveur d'écoute pour le mode passif.
 * Fournit des flux {@link BufferedReader } et {@link PrintWriter} pour le texte et {@link BufferedOutputStream} et {@link BufferedInputStream} pour le binaire.
 */
public class FTPSocket {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ServerSocket serverSocket;

    // Pour le transfert de binaires (canal de données uniquement)
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;

    // Flag pour savoir si c'est un canal de contrôle ou de données
    private boolean isControlChannel;

    /**
     * Initialise un canal de CONTRÔLE avec un socket déjà accepté par le serveur.
     * @param socket Le socket client obtenu via welcomeSocket.accept().
     * @throws IOException Si les flux ne peuvent pas être ouverts.
     */
    public FTPSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.isControlChannel = true;

        // Canal de contrôle : seulement reader et writer pour les commandes texte
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);

        // Ne pas initialiser inputStream/outputStream pour éviter les conflits
        this.inputStream = null;
        this.outputStream = null;
    }

    /**
     * Initialise un ServerSocket pour le mode passif.
     * @param serverSocket Le ServerSocket qui attend les connexions de données
     */
    public FTPSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.isControlChannel = false;
    }

    /**
     * Attend une connexion sur le serveur d'écoute.
     * @return Une nouvelle instance {@link FTPSocket} pour la connexion de données.
     * @throws IOException En cas d'erreur réseau lors de l'acceptation.
     */
    public FTPSocket accept() throws IOException {
        if (serverSocket == null) {
            throw new IllegalStateException("Ce socket n'est pas un serveur d'écoute.");
        }

        Socket dataSocket = serverSocket.accept();
        FTPSocket ftpSocket = new FTPSocket();
        ftpSocket.socket = dataSocket;
        ftpSocket.isControlChannel = false;

        // Canal de données : utiliser les flux binaires ET texte (pour LIST)
        ftpSocket.inputStream = new BufferedInputStream(dataSocket.getInputStream());
        ftpSocket.outputStream = new BufferedOutputStream(dataSocket.getOutputStream());
        ftpSocket.reader = null;
        ftpSocket.writer = new PrintWriter(dataSocket.getOutputStream(), true);

        return ftpSocket;
    }

    /**
     * Constructeur privé pour accept()
     */
    private FTPSocket() {
    }

    /**
     * Envoie une ligne de réponse (pour canal de contrôle ou LIST).
     * @param message La réponse formatée (ex: "220 Welcome").
     */
    public void write(String message) {
        if (writer == null) {
            throw new IllegalStateException("Writer non initialisé pour ce socket");
        }
        writer.println(message);
    }

    /**
     * Lit la prochaine commande (canal de contrôle uniquement).
     * @return La ligne de texte lue, ou null si la connexion est fermée.
     * @throws IOException Si une erreur réseau se produit.
     */
    public String read() throws IOException {
        if (reader == null) {
            throw new IllegalStateException("Reader non initialisé pour ce socket");
        }
        return reader.readLine();
    }

    public BufferedOutputStream getOutputStream() {
        if (outputStream == null) {
            throw new IllegalStateException("OutputStream non initialisé.");
        }
        return outputStream;
    }

    public BufferedInputStream getInputStream() {
        if (inputStream == null) {
            throw new IllegalStateException("InputStream non initialisé");
        }
        return inputStream;
    }

    public ServerSocket getServerSocket() { return serverSocket; }

    /**
     * Ferme tous les flux et sockets ouverts.
     */
    public void close() throws IOException {
        if (writer != null) writer.close();
        if (reader != null) reader.close();
        if (inputStream != null) inputStream.close();
        if (outputStream != null) outputStream.close();
        if (socket != null && !socket.isClosed()) socket.close();
        if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
    }

    public Socket getSocket() {
        if (socket == null) {
            throw new IllegalStateException("Socket n'est pas initialisé");
        }
        return socket;
    }

    public boolean isClosed() {
        if (serverSocket != null) return serverSocket.isClosed();
        return (socket == null || socket.isClosed());
    }
}