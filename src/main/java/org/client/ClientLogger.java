package org.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ClientLogger {
    public void log(String priority, String msg) throws IOException {
        try (FileWriter fileWriter = new FileWriter("src/main/java/org/client/clientlog.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("[" + priority + "]" + LocalDateTime.now() + ":" + msg + "\n");
        }
    }
}
