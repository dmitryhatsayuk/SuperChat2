package org.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class ClientLoggerTest {

    @Test
    void log() throws IOException {

        ClientLogger logger = new ClientLogger();
        logger.filePath = "src/main/resources/clientlog.txt";
        logger.log("SYS", "TEST TEXT");
        logger.log("USR", "ANOTHER TEXT");
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/clientlog.txt"));
        String firstMsg = reader.readLine();
        String secondMsg = reader.readLine();
        Boolean correct = secondMsg.contains("ANOTHER TEXT");
        Assertions.assertEquals(true, correct);


    }
}