package org.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class ServerLoggerTest {

    @Test
    void log() throws IOException {
        ServerLogger serverLogger = new ServerLogger();
        serverLogger.filePath = "src/test/java/org/server/serverlog.txt";
        serverLogger.log("SYS", "TEST TEXT");
        serverLogger.log("USR", "ANOTHER TEXT");
        BufferedReader reader = new BufferedReader(new FileReader("src/test/java/org/server/serverlog.txt"));
        String firstMsg = reader.readLine();
        String secondMsg = reader.readLine();
        Boolean correct = secondMsg.contains("ANOTHER TEXT");
        Assertions.assertEquals(true, correct);


    }
}