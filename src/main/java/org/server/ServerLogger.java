package org.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public final class ServerLogger {

    public void log(String priority, String msg) throws IOException {
        try (FileWriter fileWriter = new FileWriter("src/main/java/org/server/serverlog.txt", true); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("[" + priority + "]" + LocalDateTime.now() + ":" + msg + "\n");
            bufferedWriter.close();
            System.out.println(msg);
        }
    }

}
