package org.ftpserver.parser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandeParserTest {


    String input1;

    String input2;
    String input3;

    String input4;

    String input5;


    @BeforeEach
    void setUp() {
        input1 = "USER assane.kane";

        input2 = "PWD";

        input3 = "pass 1234";

        input4 = "  ";

        input5 = " COMMANDE arg1 arg2 arg3 arg4";
    }

    @Test
    void testCommandeSimple() {

        FTPRequest request = CommandeParser.getFTPRequest(input1);

        assertEquals("USER", request.getCommande(), "la commande \"USER\" doite être correctement");

        assertEquals(1, request.getArgs().length, "Il doit y avoir un seul argument");

        assertEquals("assane.kane", request.getArgs()[0], "L'argument doit être \"assane.kane\"");
    }

    @Test
    void testCommandeSansArguments() {
        FTPRequest request = CommandeParser.getFTPRequest(input2);

        assertEquals("PWD", request.getCommande(), "\"PWD\" est attendu");

        assertEquals(0, request.getArgs().length, "Il doit y avoir 0 argument");
    }

    @Test
    void testCasse() {
        FTPRequest request = CommandeParser.getFTPRequest(input3);

        assertEquals("PASS", request.getCommande(), "\"PASS\" <en masuscule> est attendu");
    }

    @Test
    void testNullite() {
        FTPRequest request = CommandeParser.getFTPRequest(input4);
        assertEquals("", request.getCommande(), "la commande dois etre vide");
        assertEquals(0, request.getArgs().length, "l'argument dois etre vide");
    }

    @Test
    void testPlusieursArguments() {
        FTPRequest request = CommandeParser.getFTPRequest(input5);
        assertEquals(4, request.getArgs().length, "4 arguments sont attendus");
    }

}