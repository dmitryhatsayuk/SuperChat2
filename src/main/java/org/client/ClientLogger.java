package org.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ClientLogger {
    String filePath = "src/main/resources/clientlog.txt";

    public void log(String priority, String msg) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("[" + priority + "]" + LocalDateTime.now() + ":" + msg + "\n");
        }
    }
}
