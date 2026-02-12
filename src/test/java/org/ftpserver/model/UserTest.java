package org.ftpserver.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @BeforeAll
    static void setUp() {

        User.initUser();
    }


    @Test
    void testValidAdmin() {

    }

    @Test
    void isValid() {
        assertTrue(User.isValid("admin", "123"), "l'admin doit être authentifié");
        assertTrue(User.isValid("assane.kane", "password"), "assane.kane doit être authentifié");
    }

    @Test
    void isNotValid() {
        assertFalse(User.isValid("admin", "mauvaispass"), "le mot 'est pas bon, donc il dois pas etre authentifié");
        assertFalse(User.isValid("assane", "password"), "le user assane n'existe pas");
    }

    @Test
    void isAnonymous() {
        assertTrue(User.isValid("anonymous", ""), "l'anonymous doit toujours être connecté");
        assertTrue(User.isValid("anonymous", "testtest"), "l'anonymous doit toujours être connecté");
        assertTrue(User.isValid("anonymous", "blabla"), "l'anonymous doit toujours être connecté");
        assertFalse(User.isValid("anonymou", "blabla"), "l'authentification doit échouer avec anonymou");
    }


}