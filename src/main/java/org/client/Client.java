package org.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    ClientLogger clientLogger = new ClientLogger();
    String settings = "src/main/java/org/client/settings.txt";
    final Socket socket = socketMaker(settings);
    String name;


    public Client() throws IOException {
    }

    public void start() throws IOException {
        clientLogger.log("SYS", "Soket opened");
        System.out.print("Введите ваш ник для чата:");
        name = new Scanner(System.in).nextLine();
        clientLogger.log("USR", "Used name-" + name);
        Thread sender = new Thread(msgSender, "sender");
        Thread reader = new Thread(msgReader, "reader");
        sender.start();
        reader.start();
        clientLogger.log("SYS", "Threads started");
    }

    Runnable msgReader = () -> {
        try {
            while (!this.socket.isClosed()) {
                String inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                if (inMsg != null) {
                    System.out.println(inMsg);
                    clientLogger.log("USR", "Rcvd msg " + inMsg);
                }
            }
            clientLogger.log("SYS", "Rcvd msg EXIT, Threads interrupt");
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    Runnable msgSender = () -> {
        Scanner msgScanner = new Scanner(System.in);
        try {
            while (true) {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

                String outMsg = msgScanner.nextLine();
                printWriter.println("[" + name + "]" + outMsg);
                clientLogger.log("USR", "Snd msg " + outMsg);
                if (outMsg.contains("exit")) {
                    Thread.sleep(100);
                    this.socket.close();
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    };

    public Socket socketMaker(String file) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String srcIP = reader.readLine();
        int srcPort = Integer.parseInt(reader.readLine());
        clientLogger.log("SYS", "Read IP:" + srcIP + " Read port:" + srcPort);
        return new Socket(srcIP, srcPort);
    }
}
