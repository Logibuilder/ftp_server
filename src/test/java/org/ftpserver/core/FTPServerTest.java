package org.ftpserver.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class FTPServerTest {

    private static final int TEST_PORT = 9999;

    //pour lancer le serveur dans un thread afin d'exécuter les test sinon on ne pourra pas sortir du while du serveur
    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    @AfterEach
    void tearDown() {
        executorService.shutdown();
    }

    @Test
    void testServeurStartAndAcceptConnection() throws InterruptedException, IOException {
        executorService.submit(() -> {
            try {
                FTPServer server = new FTPServer(TEST_PORT);
                server.start();
            } catch (IOException e) {  }
        });

        Thread.sleep(500);

        try (Socket clientSocket = new Socket("localhost", TEST_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            assertTrue(clientSocket.isConnected(), "Le client devrait être connecté au serveur");

            String welcomeMessage = reader.readLine();

            assertNotNull(welcomeMessage, "Le serveur devrait envoyer un message d'accueil");
            assertTrue(welcomeMessage.startsWith("220"), "Le code de retour doit être 220 (Service ready)");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void start() {
    }
}