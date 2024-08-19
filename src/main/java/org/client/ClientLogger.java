package org.superchatclient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ClientLogger {
    public void log(String priority, String msg) throws IOException {
        try (FileWriter fileWriter = new FileWriter("src/main/java/org/superchatclient/clientlog.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("[" + priority + "]" + LocalDateTime.now() + ":" + msg + "\n");
            bufferedWriter.close();
            System.out.println(msg);
        }
    }
}
