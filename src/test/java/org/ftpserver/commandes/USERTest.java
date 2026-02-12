package org.ftpserver.commandes;

import org.ftpserver.core.Client;
import org.ftpserver.core.FTPSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class COMMANDETests {
    @Mock
    protected Client clientMock;
    @Mock protected FTPSocket controlSocketMock;
    @TempDir
    protected Path tempDir;

    @BeforeEach
    void setupBase() {
        when(clientMock.getControllerSocket()).thenReturn(controlSocketMock);
        // On force le répertoire de travail sur le dossier temporaire
        System.setProperty("user.dir", tempDir.toString());
    }

    @Test
    void testUSER() {
        USER cmd = new USER();
        cmd.execute(new String[]{"admin"}, clientMock);
        verify(clientMock).setUserName("admin");
        verify(controlSocketMock).write("331 Nom d'utilisateur reçu, mot de passe demandé.");
    }

    @Test
    void testPASS_Success() {
        PASS cmd = new PASS();
        when(clientMock.getUserName()).thenReturn("admin");
        when(clientMock.checkPUser("admin", "123")).thenReturn(true);

        cmd.execute(new String[]{"123"}, clientMock);
        verify(clientMock).setAuthenticated(true);
        verify(controlSocketMock).write("230 Authentification réussie. Connexion établie.");
    }

    @Test
    void CDUPTest() throws IOException {
        Path subFolder = tempDir.resolve("folder");
        Files.createDirectories(subFolder);


        System.setProperty("user.dir", tempDir.toString());

        when(clientMock.getCurrentPath()).thenReturn("/folder");

        CDUP cdup = new CDUP();
        cdup.execute(new String[]{}, clientMock);

        verify(controlSocketMock).write("250 Repertoire change avec succes.");
        verify(clientMock).setCurrentPath("/");
    }

    @Test
    void testPWD() {
        when(clientMock.getCurrentPath()).thenReturn("/docs");
        new PWD().execute(new String[]{}, clientMock);
        verify(controlSocketMock).write("257 \"/docs\" est le répertoire actuel.");
    }

    @Test
    void testCWD_Success() throws IOException {
        Path root = Paths.get(".").toAbsolutePath().normalize();
        Path testDirPath = root.resolve("testDir_JUnit");

        if (!Files.exists(testDirPath)) {
            Files.createDirectories(testDirPath);
        }

        try {
            when(clientMock.getCurrentPath()).thenReturn("/");

            CWD cwd = new CWD();
            cwd.execute(new String[]{"testDir_JUnit"}, clientMock);

            verify(clientMock).setCurrentPath("/testDir_JUnit");
            verify(controlSocketMock).write("250 Repertoire change avec succes.");
        } finally {
            Files.deleteIfExists(testDirPath);
        }
    }

    @Test
    void testDELE_Success() throws IOException {
        Path root = Paths.get(".").toAbsolutePath().normalize();
        Path fileToDelete = root.resolve("supprimer_JUnit.txt");

        Files.createFile(fileToDelete);

        try {
            when(clientMock.getCurrentPath()).thenReturn("/");

            new DELE().execute(new String[]{"supprimer_JUnit.txt"}, clientMock);

            assertFalse(Files.exists(fileToDelete), "Le fichier devrait avoir été supprimé physiquement");
            verify(controlSocketMock).write(contains("250"));
        } finally {
            Files.deleteIfExists(fileToDelete);
        }
    }

    @Test
    void testRMD_Success() throws IOException {
        Path root = Paths.get(".").toAbsolutePath().normalize();
        Path dirToDelete = root.resolve("delete_me_JUnit");
        Files.createDirectories(dirToDelete);

        try {
            when(clientMock.getCurrentPath()).thenReturn("/");
            new RMD().execute(new String[]{"delete_me_JUnit"}, clientMock);

            assertFalse(Files.exists(dirToDelete), "Le dossier devrait être supprimé");
            verify(controlSocketMock).write(contains("250"));
        } finally {
            Files.deleteIfExists(dirToDelete);
        }
    }

    @Test
    void testRenameProcess() throws IOException {
        Path root = Paths.get(".").toAbsolutePath().normalize();
        Path sourceFile = root.resolve("ancien_JUnit.txt");
        Path destFile = root.resolve("nouveau_JUnit.txt");
        Files.createFile(sourceFile);

        try {
            when(clientMock.getCurrentPath()).thenReturn("/");

            new RNFR().execute(new String[]{"ancien_JUnit.txt"}, clientMock);
            verify(clientMock).setNameFrom(sourceFile.toString());
            verify(controlSocketMock).write("350 Pret pour RNTO");

            when(clientMock.getNameFrom()).thenReturn(sourceFile.toString());
            new RNTO().execute(new String[]{"nouveau_JUnit.txt"}, clientMock);

            assertTrue(Files.exists(destFile));
            assertFalse(Files.exists(sourceFile));
            verify(controlSocketMock).write("250 Renommage réussi.");
        } finally {
            Files.deleteIfExists(sourceFile);
            Files.deleteIfExists(destFile);
        }
    }

    @Test
    void testMDTM_Success() throws IOException {
        Path root = Paths.get(".").toAbsolutePath().normalize();
        Path file = root.resolve("time_JUnit.txt");
        Files.createFile(file);

        try {
            when(clientMock.getCurrentPath()).thenReturn("/");
            new MDTM().execute(new String[]{"time_JUnit.txt"}, clientMock);

            // Vérifie que la réponse commence par le code 213
            verify(controlSocketMock).write(contains("213 "));
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void testQUIT() throws IOException {
        new QUIT().execute(new String[]{}, clientMock);
        verify(controlSocketMock).write("221 Au revoir.");
        verify(controlSocketMock).close();
    }

    @Test
    void testLIST_RequiresPasv() throws IOException {
        when(clientMock.getDataSocket()).thenReturn(null);
        new LIST().execute(new String[]{}, clientMock);
        verify(controlSocketMock).write("503 Utilisez PASV avant LIST.");
    }

    @Test
    void testNLST_RequiresPasv() throws IOException {
        when(clientMock.getDataSocket()).thenReturn(null);
        new NLST().execute(new String[]{}, clientMock);
        verify(controlSocketMock).write("503 Utilisez PASV avant NLST.");
    }
}