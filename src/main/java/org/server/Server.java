package org.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public final int port = 435;
    //portreader("path");

    public final ServerSocket serverSocket = new ServerSocket(port);

    public Server() throws IOException {
    }

    public void run() throws IOException {
        Socket clientSocket = serverSocket.accept();
        while (true) {
            String msg = read(clientSocket);
            send(clientSocket, msg);
        }
    }

    public String read(Socket clientSocket) throws IOException {
        String msg = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())).readLine();
        System.out.println("Received " + msg);
        return msg;
    }


    public void send(Socket clientSocket, String msg) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true)) {
            printWriter.println(msg);
        }
        System.out.println("Send " + msg);

    }


    //TODO
    private int portreader(String path) {
        return 9999;
    }
}
