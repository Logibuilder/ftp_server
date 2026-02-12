package org.ftpserver.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class FTPSocketTest {


    @Mock
    Socket socketMock;

    @Mock
    ServerSocket serverMock;

    @BeforeEach
    void setUp() {

    }


    @Test
    void accept() throws IOException {
        Socket dataSocketMock = org.mockito.Mockito.mock(Socket.class);

        // On configure le dataSocket pour qu'il fournisse des flux (sinon le constructeur interne échoue)
        when(dataSocketMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(dataSocketMock.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        // On configure le serveur pour qu'il retourne ce socket quand on appelle accept()
        when(serverMock.accept()).thenReturn(dataSocketMock);

        // On crée notre FTPSocket en mode serveur
        FTPSocket serverFtpSocket = new FTPSocket(serverMock);

        //  Action : on accepte la connexion
        FTPSocket resultSocket = serverFtpSocket.accept();

        // Vérifications
        assertNotNull(resultSocket, "Le socket résultant ne doit pas être null");
        assertEquals(dataSocketMock, resultSocket.getSocket(), "Il doit contenir le socket renvoyé par le serveur");

        // Vérifier que les flux spécifiques aux données sont accessibles
        assertDoesNotThrow(resultSocket::getInputStream, "Le canal de données doit avoir un InputStream accessible");
        assertDoesNotThrow(resultSocket::getOutputStream, "Le canal de données doit avoir un OutputStream accessible");

    }

    @Test
    void write() throws IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String messageToSend = "220 Welcome to MyFTPServer";

        when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(socketMock.getOutputStream()).thenReturn(outContent);

        FTPSocket ftpSocket = new FTPSocket(socketMock);

        ftpSocket.write(messageToSend);

        assertEquals(messageToSend, outContent.toString().trim());
    }

    @Test
    void read() throws IOException {
        String inputData = "USER assane";

        when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream(inputData.getBytes()));
        when(socketMock.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        FTPSocket ftpSocket = new FTPSocket(socketMock);

        assertEquals("USER assane", ftpSocket.read());
    }

    @Test
    void getInputStream_Interdit_Sur_ControlChannel() throws IOException {
        // 1. Préparer le mock pour que le constructeur ne plante pas
        when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(socketMock.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        // 2. Créer un socket de type CONTROLE (via le constructeur standard)
        FTPSocket controlSocket = new FTPSocket(socketMock);

        // 3. Vérifier que ta sécurité fonctionne : ça DOIT lancer une erreur
        assertThrows(IllegalStateException.class, controlSocket::getInputStream, "L'accès au flux brut doit être interdit sur le canal de commande");
    }

    @Test
    void getServerSocket() throws IOException {
        FTPSocket ftpSocket = new FTPSocket(serverMock);

        assertEquals(serverMock, ftpSocket.getServerSocket(), "Devrait retourner le serveur socket mocké");

        ftpSocket.close();

        assertNull(ftpSocket.getServerSocket(), "Le serveur socket devrait être null après close");
    }

    @Test
    void close() throws IOException {
        // Préparation
        when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(socketMock.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        FTPSocket ftpSocket = new FTPSocket(socketMock);

        // Action
        ftpSocket.close();

        // Vérification
        // Selon votre code actuel, getSocket() lance une IllegalStateException si le socket est null
        assertThrows(IllegalStateException.class, ftpSocket::getSocket, "Le socket devrait être null et getSocket() devrait lever une exception");

    }

    @Test
    void getSocket() throws IOException {
        when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(socketMock.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        FTPSocket ftpSocket = new FTPSocket(socketMock);
        assertEquals(socketMock, ftpSocket.getSocket(), "Devrait retourner le socket mocké");

        ftpSocket.close();

        assertThrows(IllegalStateException.class, ftpSocket::getSocket, "l'exception doit être levée après le close");
    }

    @Test
    void isClosed() throws IOException {
        when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(socketMock.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        FTPSocket ftpSocket = new FTPSocket(socketMock);
        assertFalse(ftpSocket.isClosed());

        ftpSocket.close();
        assertTrue(ftpSocket.isClosed());
    }
}